package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.TaskDTO;
import org.cbg.projectmanagement.project_management.entity.Task;
import org.cbg.projectmanagement.project_management.mapper.TaskMapper;
import org.cbg.projectmanagement.project_management.request.TaskRequest;
import org.cbg.projectmanagement.project_management.request.TaskUpdateRequest;
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
        List<TaskDTO> taskList = taskService.findAll()
                .stream()
                .map(taskMapper::mapTaskToTaskDTO)
                .collect(Collectors.toList());

        return Response
                .status(Response.Status.OK)
                .entity(taskList)
                .build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response create(TaskRequest request) {
        Task newTask = taskService.create(request);

        return Response
                .status(Response.Status.CREATED)
                .entity(taskMapper.mapTaskToTaskDTO(newTask))
                .build();
    }

    @PATCH
    @Path("/update-progress/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response update(@PathParam("id") Long id, TaskUpdateRequest request) {
        Task updatedTask = taskService.updateProgress(id, request);
        return Response
                .status(Response.Status.OK)
                .entity(taskMapper.mapTaskToTaskDTO(updatedTask))
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
