package org.cbg.projectmanagement.project_management.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {

    private String username;
    private String password;
    private String fullName;
}
