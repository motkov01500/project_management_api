package org.cbg.projectmanagement.project_management.security;

import jakarta.security.enterprise.credential.Credential;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails implements Credential {

    private String principal;
    private Set<String> authority;
}
