package org.cbg.projectmanagement.project_management.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
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

    public Optional<User> getUserByUsername(String username) throws NotFoundResourceException {
        return userRepository
                .getUserByUsername(username);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(UserCreateDTO userCreateDTO) {
        //TODO:find role by name(better)
        Role role = roleService.findById(new Long(2));
        String hashedPassword = hashPassword(userCreateDTO.getPassword());
        User user = new User(userCreateDTO.getUsername(), hashedPassword,
                userCreateDTO.getFullName(), role);
        userRepository.create(user);
        return user;
    }

    public User updateUser(UserUpdateDTO userUpdateDTO, Long id) {
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
