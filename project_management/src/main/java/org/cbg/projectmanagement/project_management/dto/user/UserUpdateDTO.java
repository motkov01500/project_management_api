package org.cbg.projectmanagement.project_management.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserUpdateDTO {

    private String username;
    private String password;
    private String fullName;
    private String roleName;
}
