package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.task.TaskCreateDTO;
import org.cbg.projectmanagement.project_management.dto.task.TaskUpdateDTO;
import org.cbg.projectmanagement.project_management.dto.task.TaskUpdateProgressDTO;
import org.cbg.projectmanagement.project_management.mapper.TaskMapper;
import org.cbg.projectmanagement.project_management.service.TaskService;

import java.util.stream.Collectors;

@Path("/v1/task")
public class TaskController {

    @Inject
    private TaskService taskService;

    @Inject
    private TaskMapper taskMapper;

    @GET
    @Path("/administrator/get-all")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getAll() {
        return Response
                .status(Response.Status.OK)
                .entity(taskService.findAll()
                        .stream()
                        .map(taskMapper::mapTaskToTaskDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/current-user-related")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response getUserRelated() {
        return Response
                .status(Response.Status.OK)
                .entity(taskService.getTasksRelatedToCurrentUserProject()
                        .stream()
                        .map(taskMapper::mapTaskToTaskDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/administrator/get-by-id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getById(@PathParam("id") Long id) {
        return Response
                .status(Response.Status.OK)
                .entity(taskMapper
                        .mapTaskToTaskDTO(taskService.findById(id)))
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
    @RolesAllowed("user")
    public Response updateTaskProgressByUser(@PathParam("id")Long id, TaskUpdateProgressDTO taskUpdateProgressDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(taskMapper
                        .mapTaskToTaskDTO(taskService.updateProgressForUser(id,taskUpdateProgressDTO)))
                .build();
    }

    @PATCH
    @Path("/administrator/update-progress/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response updateTaskProgressByAdministrator(@PathParam("id")Long id, TaskUpdateProgressDTO taskUpdateProgressDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(taskMapper
                        .mapTaskToTaskDTO(taskService.updateProgress(id,taskUpdateProgressDTO)))
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
