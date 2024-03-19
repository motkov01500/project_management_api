package org.cbg.projectmanagement.project_management.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.repository.RoleRepository;

@Named("RoleService")
public class RoleService {

    @Inject
    RoleRepository roleRepository;

    public Role findById(Long id) {
        return roleRepository
                .findById(id);
    }
}
