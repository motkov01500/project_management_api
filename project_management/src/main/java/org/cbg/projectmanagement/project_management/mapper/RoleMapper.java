package org.cbg.projectmanagement.project_management.mapper;

import org.cbg.projectmanagement.project_management.dto.role.RoleResponseDTO;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jsr330")
public interface RoleMapper {

    RoleResponseDTO mapRoleToRoleResponseDTO(Role role);
}
