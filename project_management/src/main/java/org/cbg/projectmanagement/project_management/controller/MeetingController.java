package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingAssignUserDTO;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingCreateDTO;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingUpdateDTO;
import org.cbg.projectmanagement.project_management.mapper.MeetingMapper;
import org.cbg.projectmanagement.project_management.service.MeetingService;

import java.util.stream.Collectors;

@Path("/v1/meeting")
public class MeetingController {

    @Inject
    private MeetingService meetingService;

    @Inject
    private MeetingMapper meetingMapper;

    @GET
    @Path("/administrator/get-all")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getAll() {
        return Response
                .status(Response.Status.OK)
                .entity(meetingService.findAll()
                        .stream()
                        .map(meetingMapper::mapMeetingToMeetingDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/administrator/get-by-id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getById(@PathParam("id") Long id) {
        return  Response
                .status(Response.Status.OK)
                .entity(meetingMapper
                        .mapMeetingToMeetingDTO(meetingService.findById(id)))
                .build();
    }

    @GET
    @Path("/get-unfinished/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user","administrator"})
    public Response getUnfinishedMeetings(@PathParam("key")String key) {
        return Response
                .status(Response.Status.OK)
                .entity(meetingService.getUnfinishedMeetings(key)
                        .stream()
                        .map(meetingMapper::mapMeetingToMeetingDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/get-all-related-meetings")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response getUserRelatedMeetings() {
        return Response
                .status(Response.Status.OK)
                .entity(meetingService.getAllMeetingsToCurrentUser()
                        .stream()
                        .map(meetingMapper::mapMeetingToMeetingDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @POST
    @Path("/administrator/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response create(MeetingCreateDTO meetingCreateDTO) {
        return Response
                .status(Response.Status.CREATED)
                .entity(meetingMapper
                        .mapMeetingToMeetingDTO(meetingService.create(meetingCreateDTO)))
                .build();
    }

    @PUT
    @Path("/administrator/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response updateDate(@PathParam("id")Long id, MeetingUpdateDTO meetingUpdateDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(meetingMapper
                        .mapMeetingToMeetingDTO(meetingService.update(id, meetingUpdateDTO)))
                .build();
    }

    @PATCH
    @Path("/administrator/assign-user-to-meeting/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response addUserToMeeting(MeetingAssignUserDTO meetingAssignUserDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(meetingService.addUserToMeeting(meetingAssignUserDTO))
                .build();
    }

    @DELETE
    @Path("/administrator/delete/{id}")
    @RolesAllowed("administrator")
    public Response delete(@PathParam("id") Long id) {
        meetingService.delete(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}