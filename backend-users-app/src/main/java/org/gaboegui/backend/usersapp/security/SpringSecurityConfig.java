package org.gaboegui.backend.usersapp.security;

import org.gaboegui.backend.usersapp.security.filter.JwtAuthenticationFilter;
import org.gaboegui.backend.usersapp.security.filter.JwtValidationFilter;
import org.gaboegui.backend.usersapp.security.workfactor.BcCryptWorkFactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;


/**
 * Basic configuration for API Request methods and set of AuthFilter and passwordEncoder
 */
@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private BcCryptWorkFactorService bcCryptWorkFactorService;

    @Bean
    PasswordEncoder passwordEncoder() {
        System.out.println("Strength recommended for this hardware: "
        + bcCryptWorkFactorService.calculateStrength());
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Setup of CrossOrigin allowed requests
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration config = new CorsConfiguration();

        //config.setAllowedOrigins(List.of("http://localhost:5173/"));
        config.setAllowedOriginPatterns(List.of("*"));  // allows calls from any domain
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // will be applied to all paths from origins
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    /**
     * Registers the Cors Filter with HIGHEST_PRECEDENCE
     * @return
     */
    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter(){
        FilterRegistrationBean<CorsFilter> bean =
                new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        // for registering with high priority
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authRules -> authRules
                // only GET on this url is public, all others are forbidden
                .requestMatchers(HttpMethod.GET, "/users", "/users/page/{page}").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/users/{id}").hasRole("ADMIN")
                // all other methods ** will require admin role
                .requestMatchers( "/users/**").hasRole("ADMIN")
                .anyRequest().authenticated())
                // configure the JWT filter
                .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager())) // /login filter
                .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))     // JWT validation
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // includes the Cross Origin config calling the @Bean
                .cors( cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

}
