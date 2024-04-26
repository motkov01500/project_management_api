package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.auth.RegisterDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserCreateDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserPasswordUpdateDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserUpdateDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserUpdateImageDTO;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.exception.UsernameAlreadyExistsException;
import org.cbg.projectmanagement.project_management.exception.ValidationException;
import org.cbg.projectmanagement.project_management.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
public class UserService {

    @Inject
    private RoleService roleService;

    @Inject
    private UserRepository userRepository;

    @Context
    private SecurityContext context;

    public List<User> findAll() {
        return userRepository
                .findAll();
    }

    public List<User> getUsersRelatedToProject(String key) {
        return userRepository
                .getUsersRelatedToProject(key);
    }

    public User getCurrentUser() {
        return getUserByUsername(context.getUserPrincipal().getName());
    }

    public List<User> getUsersRelatedToMeeting(Long meetingId) {
        return userRepository
                .getUsersRelatedToMeeting(meetingId);
    }

    public User getUserByUsername(String username) throws NotFoundResourceException {
        return userRepository
                .getUserByUsername(username)
                .orElseThrow(()-> new NotFoundResourceException("User was not found!"));
    }

    public User findUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(()-> new NotFoundResourceException("User was not found"));
    }

    public List<User> getUnassignedUsers() {
        return userRepository
                .getUnassignedUsers();
    }

    public User createUser(UserCreateDTO userCreateDTO) {
        Role role = roleService.findRoleByName("user");
        String hashedPassword = hashPassword(userCreateDTO.getPassword());
        User user = new User(userCreateDTO.getUsername(), hashedPassword,
                userCreateDTO.getFullName(), role);
        userRepository.create(user);
        return user;
    }

    public User register(RegisterDTO registerDTO) {
        User user = new User();
        if(userRepository.isUserExists(registerDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        user.setUsername(registerDTO.getUsername());
        validatePassword(registerDTO.getPassword());
        user.setPassword(hashPassword(registerDTO.getPassword()));
        user.setRole(roleService.findRoleByName("user"));
        user.setFullName(registerDTO.getFullName());
        userRepository.create(user);
        return user;
    }

    @Transactional
    public User uploadImageToCurrentUser(UserUpdateImageDTO userUpdateImageDTO) {
        User currentUser = getUserByUsername(context.getUserPrincipal().getName());
        currentUser.setImageUrl(userUpdateImageDTO.getImageUrl());
        userRepository.update(currentUser);
        return currentUser;
    }

    public User updatePassword(Long id, UserPasswordUpdateDTO userPasswordUpdateDTO) {
        User user = findUserById(id);
        user.setPassword(hashPassword(userPasswordUpdateDTO.getPassword()));
        userRepository.update(user);
        return user;
    }

    public User updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User user = findUserById(id);

        if(!userUpdateDTO.getUsername().isEmpty() &&
                !(userUpdateDTO.getUsername().equals(user.getUsername()))) {
            if(userRepository.getUserByUsername(userUpdateDTO.getUsername()).isEmpty()) {
                user.setUsername(userUpdateDTO.getUsername());
            } else {
                throw new UsernameAlreadyExistsException("Username already exists");
            }
        }

        if(!(userUpdateDTO.getPassword().isEmpty()) &&
                !(userUpdateDTO.getPassword().equals(user.getPassword()))) {
            String hashedPassword = hashPassword(userUpdateDTO.getPassword());
            user.setPassword(hashedPassword);
        }

        if(!(userUpdateDTO.getFullName().isEmpty()) &&
                !(userUpdateDTO.getFullName().equals(user.getFullName()))) {
            user.setFullName(userUpdateDTO.getFullName());
        }

        if(!(userUpdateDTO.getRoleName().isEmpty()) &&
                !(userUpdateDTO.getRoleName().equals(user.getRole().getName()))) {
            user.setRole(
                    roleService.findRoleByName(userUpdateDTO.getRoleName())
            );
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

    public void validatePassword(String password) {
        Matcher matcher = Pattern.compile("(?=.*[a-z])(?=.*[A-Z]).{8,}")
                .matcher(password);
        if(!matcher.find()) {
            throw new ValidationException("Password must contains minimum one uppercase, one lowercase and " +
                    "one digit and must be at least 8 characters");
        }
    }
}
