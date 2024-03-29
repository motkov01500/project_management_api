package org.cbg.projectmanagement.project_management.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.repository.RoleRepository;

@Named("RoleService")
public class RoleService {

    @Inject
    RoleRepository roleRepository;

    public Role findRoleByName(String name) {
        return roleRepository
                .getRoleByName(name)
                .orElseThrow(() -> new NotFoundResourceException("Role is not found"
                        , Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(Json.createObjectBuilder()
                                .add("message","Role is not found")
                                .build())
                        .build()));
    }
}
