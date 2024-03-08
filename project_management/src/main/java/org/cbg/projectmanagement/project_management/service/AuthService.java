package org.cbg.projectmanagement.project_management.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.credential.BasicAuthenticationCredential;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.request.AuthRequest;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Named("AuthService")
public class AuthService {

    @Inject
    UserService userService;

    @Context
    HttpHeaders headers;

    public String validateCredentials(AuthRequest authRequest) {
        Optional<User> currentUser = userService.getUserByUsername(authRequest.getUsername());
        if(currentUser.isPresent()) {
            String userHash = currentUser.get().getPassword();
            if(userService.checkPassword(authRequest.getPassword(), userHash)) {
                String authToken = "Basic ";
                String tokenCredentials = authRequest.getUsername()  + ":" + authRequest.getPassword();
                String hashedToken = "Basic " + Base64.getEncoder().encodeToString(tokenCredentials.getBytes());
                return hashedToken;
            }
        }
        return "NOT_VALID_CREDENTIALS";
    }

    public String getCurrentUserRole() {
        String token = headers.getHeaderString("Authorization").split(" ")[1];
        byte[] tokenBytes = Base64.getDecoder().decode(token.getBytes());
        String decodedToken = new String(tokenBytes, StandardCharsets.UTF_8);
        Optional<User> currentLoggedUser = userService.getUserByUsername(decodedToken.split(":")[0]);
        return currentLoggedUser
                .get()
                .getRole()
                .getName();
    }
}
