package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.PageFilterDTO;
import org.cbg.projectmanagement.project_management.dto.Sort;
import org.cbg.projectmanagement.project_management.dto.task.*;
import org.cbg.projectmanagement.project_management.enums.SortOrder;
import org.cbg.projectmanagement.project_management.mapper.TaskMapper;
import org.cbg.projectmanagement.project_management.service.TaskService;

import java.util.stream.Collectors;

@Path("/v1/task")
public class TaskController {

    @Inject
    private TaskService taskService;

    @Inject
    private TaskMapper taskMapper;

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
                .entity(taskService.findAll(pageFilterDTO.getPage(), pageFilterDTO.getOffset()
                                , new Sort(pageFilterDTO.getSortColumn(), sortOrder))
                        .stream()
                        .map(taskMapper::mapTaskToTaskDTO)
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
                .entity(taskService.findAllSize())
                .build();
    }

    @POST
    @Path("/administrator/get-all-related-to-project/{projectKey}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getAllRelatedToProject(@PathParam("projectKey") String projectKey, PageFilterDTO pageFilterDTO) {
        SortOrder sortOrder = pageFilterDTO.getSortOrder().isEmpty() ?
                SortOrder.DEFAULT:
                SortOrder.valueOf(pageFilterDTO.getSortOrder().toUpperCase());
        return Response
                .status(Response.Status.OK)
                .entity(taskService.findAllRelatedToProject(projectKey, pageFilterDTO.getPage(), pageFilterDTO.getOffset()
                                , new Sort(pageFilterDTO.getSortColumn(), sortOrder))
                        .stream()
                        .map(taskMapper::mapTaskToTaskDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/administrator/get-all-related-to-project-size/{projectKey}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getAllRelatedToProjectSize(@PathParam("projectKey") String projectKey) {
        return Response
                .status(Response.Status.OK)
                .entity(taskService.findAllRelatedToProjectSize(projectKey))
                .build();
    }

    @POST
    @Path("/current-user-project-related/{projectKey}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response getUserRelated(@PathParam("projectKey") String projectKey, PageFilterDTO pageFilterDTO) {
        SortOrder sortOrder = pageFilterDTO.getSortOrder().isEmpty() ?
                SortOrder.DEFAULT:
                SortOrder.valueOf(pageFilterDTO.getSortOrder().toUpperCase());
        return Response
                .status(Response.Status.OK)
                .entity(taskService.getTasksRelatedToCurrentUserAndProject(projectKey, pageFilterDTO.getPage()
                                , pageFilterDTO.getOffset(), new Sort(pageFilterDTO.getSortColumn(), sortOrder))
                        .stream()
                        .map(taskMapper::mapTaskToTaskDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/current-user-project-related-size/{projectKey}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response getUserRelatedSize(@PathParam("projectKey") String projectKey) {
        return Response
                .status(Response.Status.OK)
                .entity(taskService.getTasksRelatedToCurrentUserAndProjectSize(projectKey))
                .build();
    }

    @GET
    @Path("/get-by-id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "administrator"})
    public Response getById(@PathParam("id") Long id) {
        return Response
                .status(Response.Status.OK)
                .entity(taskService.findByIdDTO(id))
                .build();
    }

    @POST
    @Path("/administrator/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response create(TaskCreateDTO taskCreateDTO) {
        return Response
                .status(Response.Status.CREATED)
                .entity(taskMapper
                        .mapTaskToTaskDTO(taskService.create(taskCreateDTO)))
                .build();
    }

    @PUT
    @Path("/administrator/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response update(@PathParam("id") Long id, TaskUpdateDTO taskUpdateDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(taskMapper
                        .mapTaskToTaskDTO(taskService.updateTask(id, taskUpdateDTO)))
                .build();
    }

    @PATCH
    @Path("/update-progress/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "administrator"})
    public Response updateTaskProgress(@PathParam("id") Long id, TaskUpdateProgressDTO taskUpdateProgressDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(taskMapper
                        .mapTaskToTaskDTO(taskService.updateProgress(id, taskUpdateProgressDTO)))
                .build();
    }

    @PATCH
    @Path("/administrator/assign-user-to-task")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response assignUserToTask(AssignUserToTaskDTO assignUserToTaskDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(taskService.assignUserToTask(assignUserToTaskDTO))
                .build();
    }

    @PATCH
    @Path("/administrator/remove-user-from-task")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response removeUserFromTask(UnAssignUserToTaskDTO unAssignUserToTaskDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(taskService.removeUserFromTask(unAssignUserToTaskDTO))
                .build();
    }

    @DELETE
    @Path("administrator/delete/{id}")
    @RolesAllowed("administrator")
    public Response delete(@PathParam("id") Long id) {
        taskService.delete(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}
