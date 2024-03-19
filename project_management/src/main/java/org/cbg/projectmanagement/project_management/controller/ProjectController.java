package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.project.ProjectCheckForUserDTO;
import org.cbg.projectmanagement.project_management.dto.project.ProjectCreateDTO;
import org.cbg.projectmanagement.project_management.dto.project.ProjectResponseDTO;
import org.cbg.projectmanagement.project_management.dto.project.ProjectUpdateDTO;
import org.cbg.projectmanagement.project_management.mapper.ProjectMapper;
import org.cbg.projectmanagement.project_management.service.ProjectService;

import java.util.stream.Collectors;

@Path("/project")
public class ProjectController {

    @Inject
    ProjectService projectService;

    @Inject
    ProjectMapper projectMapper;

    @GET
    @Path("/get-all")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getAll() {
        return Response
                .status(Response.Status.OK)
                .entity(projectService.findAll()
                        .stream()
                        .map(projectMapper::mapProjectToProjectDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getById(@PathParam("id") Long id) {
        ProjectResponseDTO projectDTO = projectMapper
                .mapProjectToProjectDTO(projectService.findById(id));
        return Response
                .status(Response.Status.OK)
                .entity(projectDTO)
                .build();
    }

    @GET
    @Path("/get-by-key/{key}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getByKey(@PathParam("key")String key) {
        return Response
                .status(Response.Status.OK)
                .entity(projectService.isUserInProject(key))
                .build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response create(ProjectCreateDTO projectCreateDTO) {
        ProjectResponseDTO createdProject = projectMapper
                .mapProjectToProjectDTO(projectService.create(projectCreateDTO));
        return Response
                .status(Response.Status.CREATED)
                .entity(createdProject)
                .build();
    }

    @PUT
    @Path("update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response update(@PathParam("id") Long id, ProjectUpdateDTO projectUpdateDTO) {
        ProjectResponseDTO updatedProject = projectMapper
                .mapProjectToProjectDTO(projectService.update(id, projectUpdateDTO));
        return Response
                .status(Response.Status.OK)
                .entity(updatedProject)
                .build();
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("administrator")
    public Response delete(@PathParam("id") Long id) {
        projectService.delete(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}
