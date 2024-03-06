package org.cbg.projectmanagement.project_management.controller;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.MeetingDTO;
import org.cbg.projectmanagement.project_management.entity.Meeting;
import org.cbg.projectmanagement.project_management.mapper.MeetingMapper;
import org.cbg.projectmanagement.project_management.request.MeetingDateUpdateRequest;
import org.cbg.projectmanagement.project_management.request.MeetingRequest;
import org.cbg.projectmanagement.project_management.request.MeetingStatusChangeRequest;
import org.cbg.projectmanagement.project_management.service.MeetingService;

@Named("MeetingController")
@Path("/meeting")
public class MeetingController {

    @Inject
    MeetingService meetingService;

    @Inject
    MeetingMapper meetingMapper;

    @GET
    @Path("get-all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response
                .status(Response.Status.OK)
                .entity(meetingService.findAll())
                .build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(MeetingRequest request) {
        Meeting newMeeting = meetingService.create(request);
        return Response
                .status(Response.Status.CREATED)
                .entity(meetingMapper.mapMeetingToMeetingDTO(newMeeting))
                .build();
    }

    @POST
    @Path("/update-status/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStatus(@PathParam("id") Long id, MeetingStatusChangeRequest request) {
        Meeting updatedMeeting = meetingService.updateStatus(id, request);
        return Response
                .status(Response.Status.OK)
                .entity(meetingMapper.mapMeetingToMeetingDTO(updatedMeeting))
                .build();
    }

    @POST
    @Path("/update-date/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDate(@PathParam("id") Long id, MeetingDateUpdateRequest request) {
        Meeting updatedMeeting = meetingService.updateDate(id, request);
        return Response
                .status(Response.Status.OK)
                .entity(meetingMapper.mapMeetingToMeetingDTO(updatedMeeting))
                .build();
    }

    @DELETE
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Long id) {
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}