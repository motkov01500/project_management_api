package org.cbg.projectmanagement.project_management.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.ProjectDTO;
import org.cbg.projectmanagement.project_management.mapper.ProjectMapper;
import org.cbg.projectmanagement.project_management.request.ProjectRequest;
import org.cbg.projectmanagement.project_management.service.ProjectService;

@Path("/project")
public class ProjectController {

    @Inject
    ProjectService projectService;

    @Inject
    ProjectMapper projectMapper;

    @GET
    @Path("/get-all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response
                .status(Response.Status.OK)
                .entity(projectService.findAll())
                .build();
    }

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        ProjectDTO projectDTO = projectMapper.mapProjectToProjectDTO(projectService.findById(id));
        return Response
                .status(Response.Status.OK)
                .entity(projectDTO)
                .build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ProjectRequest request) {
        ProjectDTO createdProject = projectService.create(request);
        return Response
                .status(Response.Status.CREATED)
                .entity(createdProject)
                .build();
    }

    @PUT
    @Path("update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id")Long id, ProjectRequest request) {
        ProjectDTO updatedProject = projectService.update(id, request);
        return Response
                .status(Response.Status.OK)
                .entity(updatedProject)
                .build();
    }

    @DELETE
    @Path("/delete/{id}")
    public Response delete(@PathParam("id")Long id) {
        projectService.delete(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}
