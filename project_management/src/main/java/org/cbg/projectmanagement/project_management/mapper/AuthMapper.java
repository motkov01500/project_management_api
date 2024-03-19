package org.cbg.projectmanagement.project_management.mapper;

import org.cbg.projectmanagement.project_management.dto.auth.AuthResponseDTO;
import org.cbg.projectmanagement.project_management.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jsr330")
public interface AuthMapper {

    AuthResponseDTO mapUserToAuthResponseDTO(User user);
}
