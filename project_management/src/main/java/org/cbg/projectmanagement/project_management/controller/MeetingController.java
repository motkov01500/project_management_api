package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingAssignUserDTO;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingCreateDTO;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingUnAssignUserDTO;
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
    @Path("/administrator/get-all/{pageNumber}/{offset}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response getAll(@PathParam("pageNumber") int pageNumber, @PathParam("offset") int offset) {
        return Response
                .status(Response.Status.OK)
                .entity(meetingService.findAll(pageNumber, offset)
                        .stream()
                        .map(meetingMapper::mapMeetingToMeetingDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/get-all-size")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "administrator"})
    public Response getAllSize() {
        return Response
                .status(Response.Status.OK)
                .entity(meetingService.findAllSize())
                .build();
    }

    @GET
    @Path("/get-current-user-meetings/{projectKey}/{pageNumber}/{offset}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"administrator", "user"})
    public Response getCurrentUserRelatedMeetings(@PathParam("projectKey") String projectKey, @PathParam("pageNumber") int page, @PathParam("offset") int offset) {
        return Response
                .status(Response.Status.OK)
                .entity(meetingService.findMeetingsRelatedToCurrentUserAndProject(projectKey, page, offset)
                        .stream()
                        .map(meetingMapper::mapMeetingToMeetingDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/get-current-user-meetings-size/{projectKey}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"administrator", "user"})
    public Response getCurrentUserRelatedMeetingsSize(@PathParam("projectKey") String projectKey) {
        return Response
                .status(Response.Status.OK)
                .entity(meetingService.findMeetingsRelatedToCurrentUserAndProjectSize(projectKey))
                .build();
    }

    @GET
    @Path("/administrator/get-by-id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user","administrator"})
    public Response getById(@PathParam("id") Long id) {
        return Response
                .status(Response.Status.OK)
                .entity(meetingMapper
                        .mapMeetingToMeetingDTO(meetingService.findById(id)))
                .build();
    }

    @GET
    @Path("/get-all-related-meetings/{page}/{offset}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response getUserRelatedMeetings(@PathParam("page") int page, @PathParam("offset") int offset) {
        return Response
                .status(Response.Status.OK)
                .entity(meetingService.getAllMeetingsToCurrentUser(page, offset)
                        .stream()
                        .map(meetingMapper::mapMeetingToMeetingDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/get-all-related-meetings-size")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response getUserRelatedMeetingsSize() {
        return Response
                .status(Response.Status.OK)
                .entity(meetingService.getAllMeetingsToCurrentUserSize())
                .build();
    }

    @GET
    @Path("/get-all-related-to-project/{projectKey}/{page}/{offset}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "administrator"})
    public Response getProjectRelatedMeetings(@PathParam("projectKey") String key, @PathParam("page") int page, @PathParam("offset") int offset) {
        return Response
                .status(Response.Status.OK)
                .entity(meetingService.findMeetingRelatedToProject(key, page, offset)
                        .stream()
                        .map(meetingMapper::mapMeetingToMeetingDTO)
                        .collect(Collectors.toList()))
                .build();
    }


    @GET
    @Path("/get-all-related-to-project-size/{projectKey}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "administrator"})
    public Response getRelatedToProjectSize(@PathParam("projectKey") String projectKey) {
        return Response
                .status(Response.Status.OK)
                .entity(meetingService.findMeetingRelatedToProjectSize(projectKey))
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
    public Response updateDate(@PathParam("id") Long id, MeetingUpdateDTO meetingUpdateDTO) {
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

    @PATCH
    @Path("/administrator/remove-user-from-meeting")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response removeUserFromMeeting(MeetingUnAssignUserDTO meetingUnAssignUserDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(meetingService.removeUserFromMeeting(meetingUnAssignUserDTO))
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