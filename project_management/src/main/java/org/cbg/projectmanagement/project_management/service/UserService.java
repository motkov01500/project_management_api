package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.Sort;
import org.cbg.projectmanagement.project_management.dto.auth.RegisterDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserCreateDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserPasswordUpdateDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserUpdateDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserUpdateImageDTO;
import org.cbg.projectmanagement.project_management.entity.*;
import org.cbg.projectmanagement.project_management.exception.*;
import org.cbg.projectmanagement.project_management.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

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

    @Inject
    private ProjectService projectService;

    @Inject
    private TaskService taskService;

    @Context
    private SecurityContext context;

    public List<User> findAll(int page, int offset, Sort sort) {
        if(sort.getColumn().isEmpty()) {
            sort.setColumn("id");
        }
        return userRepository
                .findAll(page, offset, sort)
                .stream()
                .filter(user -> !user.getUsername().equals("admin"))
                .collect(Collectors.toList());
    }

    public int findAllSize() {
        return userRepository
                .findAll(0, 0, null)
                .size();
    }

    public List<User> getUsersRelatedToProject(String key, int page, int offset, Sort sort) {
        if (sort.getColumn().isEmpty()) {
            sort.setColumn("id");
        }
        return userRepository
                .getUsersRelatedToProject(key, page, offset, sort);
    }

    public int getUsersRelatedToProjectSize(String key) {
        return userRepository
                .getUsersRelatedToProject(key, 0, 0, null)
                .size();
    }

    @Transactional
    public List<User> findUsersNotAssignedToTask(Long taskId) {
        Task currentTask = taskService.findById(taskId);
        Project project = currentTask.getProject();
        List<User> users = userRepository
                .getUsersRelatedToProjectAndNotAddedToTask(taskId, project.getKey());
        if (users.isEmpty()) {
            throw new NotFoundResourceException("Users are not available.");
        }
        return users;
    }
    @Transactional
    public int findUsersNotAssignedToTaskSize(Long taskId) {
        Task currentTask = taskService.findById(taskId);
        Project project = currentTask.getProject();
        List<User> users = userRepository
                .getUsersRelatedToProjectAndNotAddedToTask(taskId, project.getKey());
        return users.size();
    }


    public List<User> findUsersRelatedToTask(Long taskId, int page, int offset,Sort sort) {
        if(sort.getColumn().isEmpty()) {
            sort.setColumn("id");
        }
        return userRepository
                .getRelatedToTask(taskId, page, offset, sort);
    }

    public int findUsersRelatedToTaskSize(Long taskId) {
        return userRepository
                .getRelatedToTask(taskId, 0, 0, null)
                .size();
    }

    public boolean isUserExists(String username) {
        return userRepository.isUserExists(username);
    }

    @Transactional
    public boolean isUsersAvailableForAssignToTak(Long taskId) {
        Task currentTask = taskService.findById(taskId);
        Project project = currentTask.getProject();
        List<User> users = userRepository
                .getUsersRelatedToProjectAndNotAddedToTask(taskId, project.getKey());
        return !users.isEmpty();
    }

    @Transactional
    public List<User> findUsersNotAssignedToMeeting(Long meetingId) {
        Meeting meeting = meetingService.findById(meetingId);
        Project project = meeting.getProject();
        List<User> users = userRepository.getUsersRelatedToProjectAndNotAddedToMeeting(meetingId, project.getKey());
        if (users.isEmpty()) {
            throw new NotFoundResourceException("Users are not available.");
        }
        return users;
    }

    @Transactional
    public int findUsersNotAssignedToMeetingSize(Long meetingId) {
        Meeting meeting = meetingService.findById(meetingId);
        Project project = meeting.getProject();
        List<User> users = userRepository.getUsersRelatedToProjectAndNotAddedToMeeting(meetingId, project.getKey());
        return users.size();
    }

    @Transactional
    public boolean isUsersAvailableForAssignToMeeting(Long meetingId) {
        Meeting meeting = meetingService.findById(meetingId);
        Project project = meeting.getProject();
        List<User> users = userRepository.getUsersRelatedToProjectAndNotAddedToMeeting(meetingId, project.getKey());
        return !users.isEmpty();
    }

    @Transactional
    public List<User> findUsersNotAssignedToProject(String projectKey) {
        Project project = projectService.findByKey(projectKey);
        List<User> users = userRepository.getUsersNotToProject(project.getKey());
        if (users.isEmpty()) {
            throw new NotFoundResourceException("Users are not available.");
        }
        return users;
    }

    @Transactional
    public int findUsersNotAssignedToProjectSize(String projectKey) {
        Project project = projectService.findByKey(projectKey);
        List<User> users = userRepository.getUsersNotToProject(project.getKey());
        return users.size();
    }

    public boolean isUsersAvailableForAssignToProject(String projectKey) {
        Project project = projectService.findByKey(projectKey);
        List<User> users = userRepository.getUsersNotToProject(project.getKey());
        return !users.isEmpty();
    }

    public User getCurrentUser() {
        return getUserByUsername(context.getUserPrincipal().getName());
    }

    public List<User> getUsersRelatedToMeeting(Long meetingId, int page, int offset, Sort sort) {
        if(sort.getColumn().isEmpty()) {
            sort.setColumn("id");
        }
        return userRepository
                .getUsersRelatedToMeeting(meetingId, page, offset, sort);
    }

    public int getUsersRelatedToMeetingSize(Long meetingId) {
        return userRepository
                .getUsersRelatedToMeeting(meetingId, 0, 0, null)
                .size();
    }

    public User getUserByUsername(String username) throws NotFoundResourceException {
        List<User> findedUser = userRepository
                .getUserByUsername(username);
        if (findedUser.isEmpty()) {
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

    @Transactional
    public User createUser(UserCreateDTO userCreateDTO) {
        if (userRepository.isUserExists(userCreateDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        Role role = roleService.findRoleByName("user");
        String hashedPassword = hashPassword(userCreateDTO.getPassword());
        validatePassword(userCreateDTO.getPassword());
        if(!userCreateDTO.getConfirmPassword().equals(userCreateDTO.getPassword())) {
            throw new ValidationException("Passwords do not match. Please try again.");
        }
        if (userCreateDTO.getFirstName().isEmpty() || userCreateDTO.getLastName().isEmpty()) {
            throw new ValidationException("You must enter the first name and last name of user.");
        }
        User user = new User(userCreateDTO.getUsername(), hashedPassword,
                userCreateDTO.getFirstName(), userCreateDTO.getLastName(), role, Boolean.FALSE);
        user.setImageUrl("https://www.idahoagc.org/sites/default/files/default_images/default-medium.png");
        userRepository.save(user);
        return user;
    }

    public User register(RegisterDTO registerDTO) {
        User user = new User();
        if (userRepository.isUserExists(registerDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        user.setUsername(registerDTO.getUsername());
        validatePassword(registerDTO.getPassword());
        if (!registerDTO.getConfirmPassword().equals(registerDTO.getPassword())) {
            throw new ValidationException("Passwords do not match. Please try again.");
        }
        user.setPassword(hashPassword(registerDTO.getPassword()));
        user.setRole(roleService.findRoleByName("user"));
        if (registerDTO.getFirstName().trim().isEmpty() || registerDTO.getLastName().trim().isEmpty()) {
            throw new ValidationException("You must enter the first and last name of user.");
        }
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setIsDeleted(false);
        user.setImageUrl("https://www.idahoagc.org/sites/default/files/default_images/default-medium.png");
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User uploadImageToCurrentUser(UserUpdateImageDTO userUpdateImageDTO) {
        User currentUser = getUserByUsername(context.getUserPrincipal().getName());
        if (userUpdateImageDTO.getSize() > 2000000) {
            throw new ImageSizeIsTooBigException("The image size is too big(maximum 2mb).");
        }
        if (!(userUpdateImageDTO.getFileType().equals("image/jpg")) && !(userUpdateImageDTO.getFileType().contains("image/png"))
                && !(userUpdateImageDTO.getFileType().equals("image/jpeg"))) {
            throw new ImageFileTypeException("Wrong file type. Must be one of: jpg, png.");
        }
        currentUser.setImageUrl(userUpdateImageDTO.getImageUrl());
        userRepository.update(currentUser);
        return currentUser;
    }

    public boolean isUserInMeeting(String username, Long meetingId) {
        return userRepository
                .isUserInMeeting(username, meetingId);
    }

    public User updatePassword(Long id, UserPasswordUpdateDTO userPasswordUpdateDTO) {
        User user = findUserById(id);
        if (!checkPassword(userPasswordUpdateDTO.getOldPassword(), user.getPassword())) {
            throw new ValidationException("Invalid Password: The 'Old Password' you entered is incorrect. " +
                    "Please enter the correct old password to proceed.");
        }
        validatePassword(userPasswordUpdateDTO.getNewPassword());
        if (!userPasswordUpdateDTO.getNewPassword().equals(userPasswordUpdateDTO.getConfirmedNewPassword())) {
            throw new ValidationException("The 'Confirm New Password' does not match the 'New Password'." +
                    " Please re-enter to confirm your new password. ");
        }
        user.setPassword(hashPassword(userPasswordUpdateDTO.getNewPassword()));
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
            if (!userUpdateDTO.getPassword().equals(userUpdateDTO.getConfirmPassword())) {
                throw new ValidationException("Passwords do not match. Please try again.");
            }
            String hashedPassword = hashPassword(userUpdateDTO.getPassword());
            user.setPassword(hashedPassword);
        }

        if (!userUpdateDTO.getFirstName().isEmpty() && !userUpdateDTO.getFirstName().equals(user.getFirstName())) {
            user.setFirstName(userUpdateDTO.getFirstName());
        }

        if (!userUpdateDTO.getLastName().isEmpty() && !userUpdateDTO.getLastName().equals(user.getLastName())) {
            user.setLastName(userUpdateDTO.getLastName());
        }
        userRepository.update(user);
        return user;
    }

    public User defaultUpdateUser(Long id, User newUser) {
        User user = findUserById(id);
        user = newUser;
        userRepository.update(user);
        return newUser;
    }

    @Transactional
    public void deleteUserById(Long id) {
        User user = findUserById(id);
        if (user.getUsername().equals("admin")) {
            throw new DeleteAdminUserException("Admin user can't be deleted from system.");
        }
        meetingService.deleteByUsername(user.getUsername());
        projectService.deletedProjectsByUsername(user.getUsername());
        taskService.deleteTasksToUser(user.getUsername());
        userRepository.save(user);
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
