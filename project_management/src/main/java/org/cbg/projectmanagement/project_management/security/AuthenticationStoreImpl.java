package org.cbg.projectmanagement.project_management.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.service.UserService;

import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singleton;

@ApplicationScoped
public class AuthenticationStoreImpl implements IdentityStore {

    @Inject
    UserService userService;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        CredentialValidationResult result;
        UsernamePasswordCredential usernamePassword = (UsernamePasswordCredential) credential;
        User expectedUser = userService
                .getUserByUsername(usernamePassword.getCaller());
        if (userService.checkPassword(usernamePassword.getPasswordAsString(), expectedUser.getPassword())) {
            result = new CredentialValidationResult(usernamePassword.getCaller());
        } else {
            result = CredentialValidationResult.INVALID_RESULT;
        }

        return result;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return singleton(ValidationType.VALIDATE);
    }
}
