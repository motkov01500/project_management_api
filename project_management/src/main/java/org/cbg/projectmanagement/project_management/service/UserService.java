package org.cbg.projectmanagement.project_management.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.repository.UserRepository;
import org.cbg.projectmanagement.project_management.request.UpdatePasswordRequest;
import org.cbg.projectmanagement.project_management.request.UserRequest;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

@Named("UserService")
public class UserService {

    @Inject
    RoleService roleService;

    @Inject
    UserRepository userRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void createUser(UserRequest userRequest) {
        Role role = roleService.findById(new Long(4));
        String hashedPassword = hashPassword(userRequest.getPassword());
        User user = new User(userRequest.getUsername(), hashedPassword,
                userRequest.getFullName(), role);
        userRepository.create(user);
    }

    public Optional<User> getUserByUsername(String username) throws NotFoundResourceException {
        return userRepository
                .getUserByUsername(username);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUserPassword(UpdatePasswordRequest request) {
        String hashedPassword = hashPassword(request.getPassword());
        User user = findUserById(request.getId());
        user.setPassword(hashedPassword);
        userRepository.update(user);
        return user;
    }

    public void deleteUserById(Long id) {
        userRepository.delete(id);
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
