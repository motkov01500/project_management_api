package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.task_status.TaskStatusCreateDTO;
import org.cbg.projectmanagement.project_management.mapper.TaskStatusMapper;
import org.cbg.projectmanagement.project_management.service.TaskStatusService;

import java.util.stream.Collectors;

@Path("/v1/task-status")
public class TaskStatusController {

    @Inject
    private TaskStatusService taskStatusService;

    @Inject
    private TaskStatusMapper taskStatusMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/find-all")
    @RolesAllowed({"user","administrator"})
    public Response findAll() {
        return Response
                .status(Response.Status.OK)
                .entity(taskStatusService.findAll()
                        .stream()
                        .map(taskStatus -> taskStatusMapper.mapTaskResponseToTaskResponse(taskStatus))
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/find-by-name/{name}")
    @RolesAllowed({"user","administrator"})
    public Response findByName(@PathParam("name")String name){
        return Response
                .status(Response.Status.OK)
                .entity(taskStatusMapper
                        .mapTaskResponseToTaskResponse(taskStatusService.findByName(name)))
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/administrator/create")
    @RolesAllowed("administrator")
    public Response create(TaskStatusCreateDTO taskStatusCreateDTO) {
        return Response
                .status(Response.Status.CREATED)
                .entity(taskStatusMapper
                        .mapTaskResponseToTaskResponse(taskStatusService.create(taskStatusCreateDTO)))
                .build();
    }
}
