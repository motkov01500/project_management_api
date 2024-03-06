package org.cbg.projectmanagement.project_management.auth;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.request.AuthRequest;
import org.cbg.projectmanagement.project_management.request.UserRequest;
import org.cbg.projectmanagement.project_management.service.UserService;
import org.mindrot.jbcrypt.BCrypt;

@Named("AuthService")
public class AuthService {

    @Inject
    UserService userService;

    public User isValidCredentials(AuthRequest authRequest) throws NotFoundResourceException {
        User loginUser = userService.getUserByUsername(authRequest.getUsername());
        return loginUser;
    }

    private boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
