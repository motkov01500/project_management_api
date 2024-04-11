package org.cbg.projectmanagement.project_management.dto.auth;


import lombok.*;
import org.cbg.projectmanagement.project_management.dto.role.RoleResponseDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AuthResponseDTO {

    private String username;
    private RoleResponseDTO role;
}
