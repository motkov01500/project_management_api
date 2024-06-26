package org.cbg.projectmanagement.project_management.dto.auth;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AuthLoginDTO {

    private String username;
    private String password;
}
