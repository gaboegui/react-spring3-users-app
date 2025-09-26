package org.gaboegui.backend.usersapp.security;

import io.jsonwebtoken.Jwts;
import java.security.Key;

public class TokenJwtConfig {

    // JWT PRIVATE KEY changes everytime that the server is instantiated
    public static final Key SECRET_KEY = Jwts.SIG.HS512.key().build();
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    // expires in 1 hour
    public static final int JWT_TIME_EXPIRATION = 3600000;


}
