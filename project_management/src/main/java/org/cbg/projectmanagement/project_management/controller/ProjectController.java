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
    @Path("/administrator/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getById(@PathParam("id") Long id) {
        return Response
                .status(Response.Status.OK)
                .entity(projectMapper
                        .mapProjectToProjectDTO(projectService.findById(id)))
                .build();
    }

    @GET
    @Path("/administrator/get-unassigned")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getUnassignedProjects() {
        return Response
                .status(Response.Status.OK)
                .entity(projectService.findUnassignedProjects()
                        .stream()
                        .map(projectMapper::mapProjectToProjectDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @POST
    @Path("/administrator/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response create(ProjectCreateDTO projectCreateDTO) {
        return Response
                .status(Response.Status.CREATED)
                .entity(projectMapper
                        .mapProjectToProjectDTO(projectService.create(projectCreateDTO)))
                .build();
    }

    @PUT
    @Path("/administrator/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response update(@PathParam("id") Long id, ProjectUpdateDTO projectUpdateDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(projectMapper
                        .mapProjectToProjectDTO(projectService.update(id, projectUpdateDTO)))
                .build();
    }

    @DELETE
    @Path("/administrator/delete/{id}")
    @RolesAllowed("administrator")
    public Response delete(@PathParam("id") Long id) {
        projectService.delete(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}
