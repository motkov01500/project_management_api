package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.PageFilterDTO;
import org.cbg.projectmanagement.project_management.dto.Sort;
import org.cbg.projectmanagement.project_management.dto.project.ProjectAssignUserDTO;
import org.cbg.projectmanagement.project_management.dto.project.ProjectCreateDTO;
import org.cbg.projectmanagement.project_management.dto.project.ProjectUpdateDTO;
import org.cbg.projectmanagement.project_management.dto.project.UnAssignUserFromProject;
import org.cbg.projectmanagement.project_management.enums.SortOrder;
import org.cbg.projectmanagement.project_management.exception.UserAlreadyInProjectException;
import org.cbg.projectmanagement.project_management.mapper.ProjectMapper;
import org.cbg.projectmanagement.project_management.service.ProjectService;

import java.util.stream.Collectors;

@Path("/v1/project")
public class ProjectController {

    @Inject
    private ProjectService projectService;

    @Inject
    private ProjectMapper projectMapper;

    @POST
    @Path("/administrator/get-all")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getAll(PageFilterDTO pageFilterDTO) {
        SortOrder sortOrder = pageFilterDTO.getSortOrder().isEmpty() ?
                SortOrder.DEFAULT:
                SortOrder.valueOf(pageFilterDTO.getSortOrder().toUpperCase());
        return Response
                .status(Response.Status.OK)
                .entity(projectService.findAll(pageFilterDTO.getPage(), pageFilterDTO.getOffset()
                                , new Sort(pageFilterDTO.getSortColumn(), sortOrder))
                        .stream()
                        .map(projectMapper::mapProjectToProjectDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/administrator/get-all-without-paging")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getAll() {
        return Response
                .status(Response.Status.OK)
                .entity(projectService.findAllWithoutPaging()
                        .stream()
                        .map(projectMapper::mapProjectToProjectDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/administrator/get-all-size")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getAllSize() {
        return Response
                .status(Response.Status.OK)
                .entity(projectService.findAllSize())
                .build();
    }

    @GET
    @Path("/administrator/get-by-id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getById(@PathParam("id") Long id) {
        return Response
                .status(Response.Status.OK)
                .entity(projectMapper
                        .mapProjectToProjectDTO(projectService.findById(id)))
                .build();
    }

    @POST
    @Path("/get-projects-current-user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response getCurrentLoggedUserProjects(PageFilterDTO pageFilterDTO) {
        SortOrder sortOrder = pageFilterDTO.getSortOrder().isEmpty() ?
                SortOrder.DEFAULT:
                SortOrder.valueOf(pageFilterDTO.getSortOrder().toUpperCase());
        return Response
                .status(Response.Status.OK)
                .entity(projectService.findCurrentUserProjects(pageFilterDTO.getPage(), pageFilterDTO.getOffset(), new Sort(pageFilterDTO.getSortColumn(), sortOrder)))
                .build();
    }

    @GET
    @Path("/get-projects-current-user-size")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response getCurrentLoggedUserProjectsSize() {
        return Response
                .status(Response.Status.OK)
                .entity(projectService.findCurrentUserProjectsSize())
                .build();
    }

    @GET
    @Path("/get-project-by-key/{projectKey}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user","administrator"})
    public Response getProjectByKey(@PathParam("projectKey") String projectKey) {
        return Response
                .status(Response.Status.OK)
                .entity(projectMapper
                        .mapProjectToProjectDTO(projectService.findByKey(projectKey))
                ).build();
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

    @PATCH
    @Path("/administrator/assign-user-to-project")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response assignUserToProject(ProjectAssignUserDTO projectAssignUserDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(projectService
                        .assignUserToProject(projectAssignUserDTO))
                .build();
    }

    @PATCH
    @Path("/administrator/remove-user-from-project")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response removeUserFromProject(UnAssignUserFromProject unAssignUserFromProject) {
        return Response
                .status(Response.Status.OK)
                .entity(projectService
                        .removeUserFromProject(unAssignUserFromProject))
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
