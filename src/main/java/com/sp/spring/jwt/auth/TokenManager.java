package com.sp.spring.jwt.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenManager {
    private static final int validity = 5*60*1000; //ms
    private static final String SecretKey = "SerkanP";

    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public String generateToken(String username){
        long CurrentTime = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("SerkanPolat") //Kim Imzaladi
                .setIssuedAt(new Date(CurrentTime)) //ne zaman imzalandi
                .setExpiration(new Date(CurrentTime+validity)) //Gecerlilik sures
                .signWith(key).compact();
    }
    public boolean tokenValidate(String token){
        if(getUserFromToken(token)!=null && isExpired(token)){
            return true;
        }
        return false;
    }
    public String getUserFromToken(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(SecretKey).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
    public boolean isExpired(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date(System.currentTimeMillis()));
    }
}
