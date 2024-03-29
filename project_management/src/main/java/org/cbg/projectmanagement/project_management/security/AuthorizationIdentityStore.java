package org.cbg.projectmanagement.project_management.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.service.UserService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singleton;

@ApplicationScoped
public class AuthorizationIdentityStore implements IdentityStore {

    @Inject
    UserService userService;

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        Set<String> result = new HashSet<>();
        User currentUser = userService.getUserByUsername(validationResult.getCallerPrincipal().getName());
        result.add(currentUser.getRole().getName());
        return result;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return singleton(ValidationType.PROVIDE_GROUPS);
    }
}
