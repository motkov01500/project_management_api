package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.task.TaskCreateDTO;
import org.cbg.projectmanagement.project_management.dto.task.TaskResponseDTO;
import org.cbg.projectmanagement.project_management.dto.task.TaskUpdateProgressDTO;
import org.cbg.projectmanagement.project_management.mapper.TaskMapper;
import org.cbg.projectmanagement.project_management.service.TaskService;

import java.util.List;
import java.util.stream.Collectors;

@Path("/task")
public class TaskController {

    @Inject
    TaskService taskService;

    @Inject
    TaskMapper taskMapper;

    @GET
    @Path("/get-all")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getAll() {
        List<TaskResponseDTO> taskList = taskService.findAll()
                .stream()
                .map(taskMapper::mapTaskToTaskDTO)
                .collect(Collectors.toList());
        return Response
                .status(Response.Status.OK)
                .entity(taskList)
                .build();
    }

    @GET
    @Path("/get-user")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getUser() {
        return Response
                .status(Response.Status.OK)
                .entity(taskService.userName())
                .build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response create(TaskCreateDTO taskCreateDTO) {
        TaskResponseDTO newTask = taskMapper
                .mapTaskToTaskDTO(taskService.create(taskCreateDTO));
        return Response
                .status(Response.Status.CREATED)
                .entity(newTask)
                .build();
    }

    //TODO:For user which is current project
    @PATCH
    @Path("/update-progress/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response update(@PathParam("id")Long id, TaskUpdateProgressDTO taskUpdateProgressDTO) {
        TaskResponseDTO updatedTask = taskMapper
                .mapTaskToTaskDTO(taskService.updateProgress(id, taskUpdateProgressDTO));
        return Response
                .status(Response.Status.OK)
                .entity(updatedTask)
                .build();
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("administrator")
    public Response delete(@PathParam("id") Long id) {
        taskService.delete(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}
