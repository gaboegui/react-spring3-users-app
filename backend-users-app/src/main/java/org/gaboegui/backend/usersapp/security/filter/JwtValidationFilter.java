package org.gaboegui.backend.usersapp.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.gaboegui.backend.usersapp.security.SimpleGrantedAuthorityJsonCreator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;

import static org.gaboegui.backend.usersapp.security.TokenJwtConfig.*;

/**
 * The Filter executes in every Request, hierarchy: OncePerRequestFilter
 */
public class JwtValidationFilter extends BasicAuthenticationFilter {


    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_AUTHORIZATION);

        if (header == null || !header.startsWith(PREFIX_TOKEN)){
            // continues without any action
            chain.doFilter(request,response);
            return;
        }

        String token = header.replace(PREFIX_TOKEN,"");


        try {
            //verify valid Jwt
            //String username = Jwts.parser().verifyWith((SecretKey) SECRET_KEY).build().parseSignedClaims(token).getPayload().getSubject();
            Claims claims = (Claims) Jwts.parser().verifyWith((SecretKey) SECRET_KEY).build()
                    .parseSignedClaims(token).getPayload();

            String username = claims.getSubject();
            Object rolesFromJwt = claims.get("authorities");

            // convert the json roles from Jwt in GrantedAuthority
            List<GrantedAuthority> roles = Arrays.asList(
                        new ObjectMapper()
                            // added to avoid MismatchedInputException
                            .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                            .readValue(rolesFromJwt.toString().getBytes(), SimpleGrantedAuthority[].class));

            // credentials correspond to password
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null,roles);

            // adds the validated user to Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // continues
            chain.doFilter(request,response);

        } catch (JwtException exception){
            Map<String, String> body = new HashMap<>();
            body.put("message", "The JWT token provided is not valid");
            body.put("error", exception.getMessage());


            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(401);
            response.setContentType("application/json");
       }

    }
}
