package org.cbg.projectmanagement.project_management.controller;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.request.RoleRequest;
import org.cbg.projectmanagement.project_management.service.RoleService;

import java.awt.*;
import java.util.List;

@Named("RoleController")
@Path("role")
public class RoleController {

    @Inject
    RoleService roleService;

    @GET
    @Produces("application/json")
    @Path("{id}")
    public Role getById(@PathParam("id") Long id) {
        return roleService.findById(id);
    }

    @GET
    @Produces("application/json")
    @Path("/all")
    public List<Role> getAll() {
        return roleService.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/create")
    public void create(RoleRequest request) {
         roleService.createRole(request);
    }
}
