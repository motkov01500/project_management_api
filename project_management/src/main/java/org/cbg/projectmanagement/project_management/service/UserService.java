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
import org.cbg.projectmanagement.project_management.entity.Meeting;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.exception.*;
import org.cbg.projectmanagement.project_management.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Stateless
public class UserService {

    @Inject
    private RoleService roleService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MeetingService meetingService;

    @Context
    private SecurityContext context;

    public List<User> findAll() {
        return userRepository
                .findAll()
                .stream()
                .filter(user -> !user.getUsername().equals("admin"))
                .collect(Collectors.toList());
    }

    public List<User> getUsersRelatedToProject(String key) {
        return userRepository
                .getUsersRelatedToProject(key);
    }

    @Transactional
    public List<User> findUsersNotAssignedToMeeting(Long meetingId) {
        Meeting meeting = meetingService.findById(meetingId);
        Project project = meeting.getProject();
        List<User> users = getUsersRelatedToProject(project.getKey())
                .stream()
                .filter(user -> !(meetingService.isUserAssignedToMeeting(meetingId, user.getId())))
                .collect(Collectors.toList());
        return users;
    }

    public User getCurrentUser() {
        return getUserByUsername(context.getUserPrincipal().getName());
    }

    public List<User> getUsersRelatedToMeeting(Long meetingId) {
        return userRepository
                .getUsersRelatedToMeeting(meetingId);
    }

    public User getUserByUsername(String username) throws NotFoundResourceException {
        List<User> findedUser = userRepository
                .getUserByUsername(username);
        if(findedUser.isEmpty()) {
            throw new NotFoundResourceException("User was not found!");
        }
        return findedUser.get(0);
    }

    public User findUserById(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundResourceException("User was not found"));
        if (user.getIsDeleted() == Boolean.TRUE) {
            throw new NotFoundResourceException("User was not found");
        }
        return user;
    }

    public List<User> getUnassignedUsers() {
        return userRepository
                .getUnassignedUsers();
    }

    @Transactional
    public User createUser(UserCreateDTO userCreateDTO) {
        if (userRepository.isUserExists(userCreateDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        Role role = roleService.findRoleByName("user");
        String hashedPassword = hashPassword(userCreateDTO.getPassword());
        validatePassword(userCreateDTO.getPassword());
        User user = new User(userCreateDTO.getUsername(), hashedPassword,
                userCreateDTO.getFullName(), role, Boolean.FALSE);
        user.setImageUrl("https://www.idahoagc.org/sites/default/files/default_images/default-medium.png");
        userRepository.create(user);
        return user;
    }

    public User register(RegisterDTO registerDTO) {
        User user = new User();
        if (userRepository.isUserExists(registerDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        user.setUsername(registerDTO.getUsername());
        validatePassword(registerDTO.getPassword());
        user.setPassword(hashPassword(registerDTO.getPassword()));
        user.setRole(roleService.findRoleByName("user"));
        if(registerDTO.getFullName().trim().isEmpty()) {
            throw new ValidationException("You must enter the full name of user.");
        }
        user.setFullName(registerDTO.getFullName());
        user.setIsDeleted(false);
        user.setImageUrl("https://www.idahoagc.org/sites/default/files/default_images/default-medium.png");
        userRepository.create(user);
        return user;
    }

    @Transactional
    public User uploadImageToCurrentUser(UserUpdateImageDTO userUpdateImageDTO) {
        User currentUser = getUserByUsername(context.getUserPrincipal().getName());
        if(userUpdateImageDTO.getSize() > 2000000) {
            throw new ImageSizeIsTooBigException("The image size is too big(maximum 2mb).");
        }
        if(!(userUpdateImageDTO.getFileType().equals("image/jpg")) && !(userUpdateImageDTO.getFileType().contains("image/png"))
        && !(userUpdateImageDTO.getFileType().equals("image/jpeg"))) {
            throw new ImageFileTypeException("Wrong file type. Must be one of: jpg, png.");
        }
        currentUser.setImageUrl(userUpdateImageDTO.getImageUrl());
        userRepository.update(currentUser);
        return currentUser;
    }

    public User updatePassword(Long id, UserPasswordUpdateDTO userPasswordUpdateDTO) {
        User user = findUserById(id);
        if (!checkPassword(userPasswordUpdateDTO.getOldPassword(), user.getPassword())) {
            throw new ValidationException("Invalid Password: The 'Old Password' you entered is incorrect. " +
                    "Please enter the correct old password to proceed.");
        }
        validatePassword(userPasswordUpdateDTO.getNewPassword());
        if(!userPasswordUpdateDTO.getNewPassword().equals(userPasswordUpdateDTO.getConfirmedNewPassword())) {
            throw new ValidationException("The 'Confirm New Password' does not match the 'New Password'." +
                    " Please re-enter to confirm your new password. ");
        }
        user.setPassword(userPasswordUpdateDTO.getNewPassword());
        userRepository.update(user);
        return user;
    }

    public User updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User user = findUserById(id);

        if (!userUpdateDTO.getUsername().isEmpty() &&
                !(userUpdateDTO.getUsername().equals(user.getUsername()))) {
            if (userRepository.isUserExists(userUpdateDTO.getUsername())) {
                throw new UsernameAlreadyExistsException("Username already exists");
            } else {
                user.setUsername(userUpdateDTO.getUsername());
            }
        }

        if (!(userUpdateDTO.getPassword().isEmpty()) &&
                !(userUpdateDTO.getPassword().equals(user.getPassword()))) {
            validatePassword(userUpdateDTO.getPassword());
            String hashedPassword = hashPassword(userUpdateDTO.getPassword());
            user.setPassword(hashedPassword);
        }

        if (!(userUpdateDTO.getFullName().isEmpty()) &&
                !(userUpdateDTO.getFullName().equals(user.getFullName()))) {
            user.setFullName(userUpdateDTO.getFullName());
        }

        if (!(userUpdateDTO.getRoleName().isEmpty()) &&
                !(userUpdateDTO.getRoleName().equals(user.getRole().getName()))) {
            user.setRole(
                    roleService.findRoleByName(userUpdateDTO.getRoleName())
            );
        }

        userRepository.update(user);
        return user;
    }

    public void deleteUserById(Long id) {
        User user = findUserById(id);
        if (user.getUsername().equals("admin")) {
            throw new DeleteAdminUserException("Admin user can't be deleted from system.");
        }
        userRepository.delete(id);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

    private void validatePassword(String password) {
        Matcher matcher = Pattern.compile("(?=.*[a-z])(?=.*[A-Z]).{8,}")
                .matcher(password);
        if (!matcher.find()) {
            throw new ValidationException("Password must contains minimum one uppercase, one lowercase and " +
                    "one digit and must be at least 8 characters");
        }
    }
}
