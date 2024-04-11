package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.repository.RoleRepository;

@Stateless
public class RoleService {

    @Inject
    private RoleRepository roleRepository;

    public Role findRoleByName(String name) {
        return roleRepository
                .getRoleByName(name)
                .orElseThrow(() -> new NotFoundResourceException(
                        Response
                                .status(Response.Status.NOT_FOUND)
                                .entity(Json.createObjectBuilder()
                                        .add("message", "Role was not found")
                                        .build())
                                .build()));
    }
}
