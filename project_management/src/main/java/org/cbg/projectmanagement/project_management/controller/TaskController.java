package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.task.TaskCreateDTO;
import org.cbg.projectmanagement.project_management.dto.task.TaskUpdateProgressDTO;
import org.cbg.projectmanagement.project_management.mapper.TaskMapper;
import org.cbg.projectmanagement.project_management.service.TaskService;

import java.util.stream.Collectors;

@Path("/task")
public class TaskController {

    @Inject
    TaskService taskService;

    @Inject
    TaskMapper taskMapper;

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

    @PATCH
    @Path("/user/update-progress/{id}")
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
