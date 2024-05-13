package org.cbg.projectmanagement.project_management.dto.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class UserPasswordUpdateDTO {

    private String oldPassword;
    private String newPassword;
    private String confirmedNewPassword;
}
