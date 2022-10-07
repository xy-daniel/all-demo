package com.daniel.zuul.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Autowired
    private JwtConfiguration jwtConfiguration;

    /**
     * token封装
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtConfiguration.getKey())
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
