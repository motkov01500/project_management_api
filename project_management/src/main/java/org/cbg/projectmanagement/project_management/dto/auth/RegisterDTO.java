package org.cbg.projectmanagement.project_management.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class RegisterDTO {

    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
}
