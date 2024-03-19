package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingCreateDTO;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingResponseDTO;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingUpdateDTO;
import org.cbg.projectmanagement.project_management.mapper.MeetingMapper;
import org.cbg.projectmanagement.project_management.service.MeetingService;

import java.util.List;
import java.util.stream.Collectors;

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
    @RolesAllowed("administrator")
    public Response getAll() {
        List<MeetingResponseDTO> meetingList = meetingService.findAll()
                .stream()
                .map(meetingMapper::mapMeetingToMeetingDTO)
                .collect(Collectors.toList());
        return Response
                .status(Response.Status.OK)
                .entity(meetingList)
                .build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response create(MeetingCreateDTO meetingCreateDTO) {
        MeetingResponseDTO newMeeting = meetingMapper.mapMeetingToMeetingDTO(
                meetingService.create(meetingCreateDTO));
        return Response
                .status(Response.Status.CREATED)
                .entity(newMeeting)
                .build();
    }

    @POST
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrator")
    public Response updateDate(@PathParam("id")Long id, MeetingUpdateDTO meetingUpdateDTO) {
        MeetingResponseDTO updatedMeeting = meetingMapper
                .mapMeetingToMeetingDTO(meetingService.update(id, meetingUpdateDTO));
        return Response
                .status(Response.Status.OK)
                .entity(updatedMeeting)
                .build();
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("administrator")
    public Response delete(@PathParam("id") Long id) {
        meetingService.delete(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}