package org.cbg.projectmanagement.project_management.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.repository.RoleRepository;
import org.cbg.projectmanagement.project_management.request.RoleRequest;

import jakarta.transaction.Transactional;
import java.util.List;

@Named("RoleService")
public class RoleService {

    @Inject
    RoleRepository roleRepository;

    public Role findById(Long id) {
        return roleRepository.findById(id);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional
    public void createRole(RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        roleRepository.create(role);
    }
}
