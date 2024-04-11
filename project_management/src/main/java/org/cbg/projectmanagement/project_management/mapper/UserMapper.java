package org.cbg.projectmanagement.project_management.mapper;

import org.cbg.projectmanagement.project_management.dto.user.UserResponseDTO;
import org.cbg.projectmanagement.project_management.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jsr330")
public interface UserMapper {

    UserResponseDTO userToUserResponseDTO(User user);
}
