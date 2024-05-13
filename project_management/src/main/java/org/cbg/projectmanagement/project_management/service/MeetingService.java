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
import org.cbg.projectmanagement.project_management.exception.MeetingOutdatedException;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.exception.ValidationException;
import org.cbg.projectmanagement.project_management.repository.MeetingRepository;

import java.time.LocalDateTime;
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

    public List<Meeting> getUnfinishedMeetings(String key) {
        return meetingRepository
                .getUpcomingMeetings(key);
    }

    public Meeting findById(Long id) {
        Meeting meeting = meetingRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Meeting was not found"));
        if (meeting.getIsDeleted()) {
            throw new NotFoundResourceException("Meeting was not found");
        }
        return meeting;
    }

    public List<Meeting> findMeetingRelatedToProject(String projectKey) {
        Project project = projectService.findByKey(projectKey);
        List<Meeting> meetings = meetingRepository
                .getMeetingsRelatedToProject(projectKey);
        if(meetings.isEmpty()) {
            throw new NotFoundResourceException("Meetings are not found for current project.");
        }
        return meetingRepository
                .getMeetingsRelatedToProject(projectKey);
    }

    public List<Meeting> findMeetingsRelatedToCurrentUserAndProject(String projectKey) {
        return meetingRepository
                .getMeetingsRelatedToProjectAndUser(context.getUserPrincipal().getName(), projectKey);
    }

    public boolean isUserAssignedToMeeting(Long meetingId, Long userId) {
        return meetingRepository.isUserInMeeting(meetingId,userId);
    }

    @Transactional
    public Meeting create(MeetingCreateDTO meetingCreateDTO) {
        Project project = projectService.findById(meetingCreateDTO.getProjectId());
        Meeting newMeeting = new Meeting(meetingCreateDTO.getDate(), meetingCreateDTO.getTitle(),
                project, Boolean.FALSE);
        newMeeting.setTitle(meetingCreateDTO.getTitle());
        meetingRepository.create(newMeeting);
        return newMeeting;
    }

    public Meeting update(Long id, MeetingUpdateDTO meetingUpdateDTO) {
        Meeting updatedMeeting = findById(id);
        if(meetingUpdateDTO.getDate() != null) {
            updatedMeeting.setDate(meetingUpdateDTO.getDate());
        }
        if(meetingUpdateDTO.getTitle() != null) {
            updatedMeeting.setTitle(meetingUpdateDTO.getTitle());
        }
        if(meetingUpdateDTO.getDate() != null && meetingUpdateDTO.getTitle() != null) {
            throw new ValidationException("All values are empty.");
        }
        meetingRepository
                .update(updatedMeeting);
        return updatedMeeting;
    }

    @Transactional
    public JsonObject addUserToMeeting(MeetingAssignUserDTO meetingAssignUserDTO) {
        User userToAssign = userService.findUserById(meetingAssignUserDTO.getUserId());
        Meeting meeting = findById(meetingAssignUserDTO.getMeetingId());
        if(meeting.getDate().isBefore(LocalDateTime.now())) {
            throw new MeetingOutdatedException("Meeting is outdated.");
        }
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

    public void deleteRelatedToProjectMeetings(Long projectId) {
        meetingRepository.deleteByProjectId(projectId);
    }

    private String validateMeetingStatus(LocalDateTime localDateTime, int minutesDuration) {
        String status = "";
        if(localDateTime.isBefore(LocalDateTime.now())) {
            status =  "Finished";
        }
        if(localDateTime.isAfter(LocalDateTime.now())) {
            status = "Upcoming";
        }
        if(localDateTime.isAfter(LocalDateTime.now()) && localDateTime.isBefore(LocalDateTime.now().plusMinutes(minutesDuration))) {
            status = "Started";
        }
        return status;
    }
}
