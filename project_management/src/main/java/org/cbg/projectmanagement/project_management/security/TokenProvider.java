package org.cbg.projectmanagement.project_management.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.service.UserService;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Stateless
public class TokenProvider {

    @Inject
    private UserService userService;

    public String createToken(String username, Set<String> authorities) {
        long now = new Date().getTime();
        return Jwts.builder()
                .setSubject(username)
                .claim("auth", authorities.stream().collect(Collectors.joining("")))
                .signWith(SignatureAlgorithm.HS512, "my-secret")
                .setExpiration(new Date(now + TimeUnit.MINUTES.toMillis(90)))
                .compact();
    }

    public UserDetails getUserDetails(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey("my-secret")
                .parseClaimsJws(token)
                .getBody();

        Set<String> authority = new HashSet<>();
        authority.add(claims.get("auth").toString());
        return new UserDetails(claims.getSubject(), authority);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey("my-secret").parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            return false;
        }
    }
}
