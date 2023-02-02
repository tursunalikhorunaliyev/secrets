package com.testsecurity.secrets.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtService {

    private final String secret = "khorunaliyev";
    private final long expirationTime = 120000;


    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime()+expirationTime);

        return Jwts.builder().setSubject(username).setExpiration(expirationDate).setIssuedAt(currentDate).signWith(SignatureAlgorithm.HS256, secret).compact();
    }


    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    public String getTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(token.startsWith("Bearer ")){
            return token.substring(7);
        }
        else{
            return null;
        }
    }

    public boolean isTokenExpired(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            return true;
        }
        catch (ExpiredJwtException e){
            return false;
        }
    }
}
