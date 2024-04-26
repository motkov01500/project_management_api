package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingAssignUserDTO;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingCreateDTO;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingUpdateDTO;
import org.cbg.projectmanagement.project_management.entity.Meeting;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.repository.MeetingRepository;

import java.util.List;

@Stateless
public class MeetingService {

    @Inject
    private MeetingRepository meetingRepository;

    @Inject
    private ProjectService projectService;

    @Inject
    private UserService userService;

    @Context
    private SecurityContext context;


    public List<Meeting> findAll() {
        return meetingRepository
                .findAll();
    }

    public List<Meeting> getAllMeetingsToCurrentUser() {
        return meetingRepository
                .getCurrentUserMeetings(context.getUserPrincipal().getName());
    }

    public Meeting findById(Long id) {
        return meetingRepository
                .findById(id)
                .orElseThrow(()-> new NotFoundResourceException("Meeting was not found"));
    }

    @Transactional
    public  Meeting create(MeetingCreateDTO meetingCreateDTO) {
        Project project = projectService.findById(meetingCreateDTO.getProjectId());
        Meeting newMeeting = new Meeting(meetingCreateDTO.getDate(), meetingCreateDTO.getStatus(), project);
        meetingRepository.create(newMeeting);
        return newMeeting;
    }

    public Meeting update(Long id, MeetingUpdateDTO meetingUpdateDTO) {
        Meeting updatedMeeting = findById(id);
        updatedMeeting.setDate(meetingUpdateDTO.getDate());
        updatedMeeting.setStatus(meetingUpdateDTO.getStatus());
        meetingRepository
                .update(updatedMeeting);
        return updatedMeeting;
    }

    @Transactional
    public JsonObject addUserToMeeting(MeetingAssignUserDTO meetingAssignUserDTO) {
        User userToAssign = userService.findUserById(meetingAssignUserDTO.getUserId());
        Meeting meeting  = findById(meetingAssignUserDTO.getMeetingId());
        meeting.getUsers().add(userToAssign);
        return Json.createObjectBuilder()
                .add("message", "User is added to meeting!")
                .build();
    }

    @Transactional
    public void delete(Long id) {
        meetingRepository
                .delete(id);
    }

    public List<Meeting> getUnfinishedMeetings(String key) {
        return meetingRepository
                .getUnfinishedMeetings(key);
    }
}
