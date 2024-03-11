package org.cbg.projectmanagement.project_management.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import jakarta.inject.Named;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Named("TokenProvider")
public class TokenProvider {

    public String createToken(String username, Set<String> authorities) {
        long now = new Date().getTime();

        return Jwts.builder()
                .setSubject(username)
                .claim("auth", authorities.stream().collect(Collectors.joining("")))
                .signWith(SignatureAlgorithm.HS512, ConstantProperties.SECRET_KEY)
                .setExpiration(new Date(now + ConstantProperties.TOKEN_VALIDITY))
                .compact();
    }

    public UserDetails getUserDetails(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(ConstantProperties.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        Set<String> authority = new HashSet<>();
        authority.add(claims.get("auth").toString());
        return new UserDetails(claims.getSubject(), authority);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(ConstantProperties.SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            return false;
        }
    }
}
