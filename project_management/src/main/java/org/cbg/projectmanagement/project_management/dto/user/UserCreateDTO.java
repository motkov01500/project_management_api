package org.cbg.projectmanagement.project_management.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserCreateDTO {

    private String username;
    private String password;
    private String fullName;
}
