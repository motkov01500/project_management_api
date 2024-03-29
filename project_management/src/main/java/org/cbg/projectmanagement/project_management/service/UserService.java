package org.cbg.projectmanagement.project_management.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.user.UserCreateDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserUpdateDTO;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.repository.UserRepository;
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

    public User getUserByUsername(String username) throws NotFoundResourceException {
        return userRepository
                .getUserByUsername(username)
                .orElseThrow(()-> new NotFoundResourceException("User is not found", Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(Json.createObjectBuilder()
                                .add("message","User is not found!")
                                .build())
                        .build()));
    }

    public User findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getUnassignedUsers(String key) {
        return userRepository
                .getUnassignedUsers(key);
    }

    public User createUser(UserCreateDTO userCreateDTO) {
        Role role = roleService.findRoleByName("user");
        String hashedPassword = hashPassword(userCreateDTO.getPassword());
        User user = new User(userCreateDTO.getUsername(), hashedPassword,
                userCreateDTO.getFullName(), role);
        userRepository.create(user);
        return user;
    }

    public User updateUser(Long id,UserUpdateDTO userUpdateDTO) {
        User user = findUserById(id);
        if(!userUpdateDTO.getPassword().isEmpty()) {
            String hashedPassword = hashPassword(userUpdateDTO.getPassword());
            user.setPassword(hashedPassword);
        }

        if(!userUpdateDTO.getFullName().isEmpty()) {
            user.setFullName(userUpdateDTO.getFullName());
        }

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
