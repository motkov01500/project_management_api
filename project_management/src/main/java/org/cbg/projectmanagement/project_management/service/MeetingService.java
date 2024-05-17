package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingAssignUserDTO;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingCreateDTO;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingUnAssignUserDTO;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingUpdateDTO;
import org.cbg.projectmanagement.project_management.entity.Meeting;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.exception.MeetingOutdatedException;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.exception.UserIsAlreadyAssignedToMeetingException;
import org.cbg.projectmanagement.project_management.exception.ValidationException;
import org.cbg.projectmanagement.project_management.mapper.MeetingMapper;
import org.cbg.projectmanagement.project_management.repository.MeetingRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless
public class MeetingService {

    @Inject
    private MeetingRepository meetingRepository;

    @Inject
    private ProjectService projectService;

    @Inject
    private MeetingMapper meetingMapper;

    @Inject
    private UserService userService;

    @Context
    private SecurityContext context;

    public List<Meeting> findAll(int pageNumber, int offsite) {
        List<Meeting> meetings = meetingRepository
                .findAll(pageNumber, offsite);
        return meetings;
    }

    public int findAllSize() {
        return meetingRepository
                .findAll(0, 0)
                .size();
    }

    public List<Meeting> getAllMeetingsToCurrentUser(int pageNumber, int offsite) {
        List<Meeting> meetings = meetingRepository
                .getCurrentUserMeetings(context.getUserPrincipal().getName(), pageNumber, offsite);
        return meetings;
    }

    public int getAllMeetingsToCurrentUserSize() {
        return meetingRepository
                .getCurrentUserMeetings(context.getUserPrincipal().getName(), 0, 0)
                .size();
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

    public List<Meeting> findMeetingRelatedToProject(String projectKey, int pageNumber, int offset) {
        Project project = projectService.findByKey(projectKey);
        List<Meeting> meetings = meetingRepository
                .getMeetingsRelatedToProjectWithPaging(projectKey, pageNumber, offset);
        if (meetings.isEmpty()) {
            throw new NotFoundResourceException("Meetings are not found for current project.");
        }
        return meetings;
    }

    public int findMeetingRelatedToProjectSize(String projectKey) {
        Project project = projectService.findByKey(projectKey);
        List<Meeting> meetings = meetingRepository
                .getMeetingsRelatedToProjectWithPaging(projectKey, 0, 0);
        return meetings.size();
    }

    public List<Meeting> findMeetingsRelatedToCurrentUserAndProject(String projectKey, int page, int offset) {
        List<Meeting> meetings = meetingRepository
                .getMeetingsRelatedToProjectAndUser(context.getUserPrincipal().getName(), projectKey, page, offset);
        return meetings;
    }

    public int findMeetingsRelatedToCurrentUserAndProjectSize(String projectKey) {
        return meetingRepository
                .getMeetingsRelatedToProjectAndUser(context.getUserPrincipal().getName(), projectKey, 0, 0)
                .size();
    }

    public List<Meeting> findMeetingsRelatedToUserAndProject(String projectKey, String username) {
        return meetingRepository
                .getMeetingsRelatedToProjectAndUser(username, projectKey,0,0);
    }

    public boolean isUserAssignedToMeeting(Long meetingId, Long userId) {
        return meetingRepository.isUserInMeeting(meetingId, userId);
    }

    @Transactional
    public Meeting create(MeetingCreateDTO meetingCreateDTO) {
        Project project = projectService.findById(meetingCreateDTO.getProjectId());
        Meeting newMeeting = new Meeting(meetingCreateDTO.getDate(), meetingCreateDTO.getTitle(),
                project, Boolean.FALSE);
        newMeeting.setTitle(meetingCreateDTO.getTitle());
        meetingRepository.save(newMeeting);
        return newMeeting;
    }

    public Meeting update(Long id, MeetingUpdateDTO meetingUpdateDTO) {
        Meeting updatedMeeting = findById(id);
        if (!meetingUpdateDTO.getDate().isEmpty()) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime localDateTime = LocalDateTime.parse(meetingUpdateDTO.getDate(),dateTimeFormatter);
            updatedMeeting.setDate(localDateTime);
        }
        if (!meetingUpdateDTO.getTitle().isEmpty()) {
            updatedMeeting.setTitle(meetingUpdateDTO.getTitle());
        }
        meetingRepository
                .update(updatedMeeting);
        return updatedMeeting;
    }

    public void defaultUpdate(Long meetingId, Meeting newMeeting) {
        Meeting currentMeeting = findById(meetingId);
        currentMeeting = newMeeting;
        meetingRepository.save(currentMeeting);
    }

    @Transactional
    public JsonObject removeUserFromMeeting(MeetingUnAssignUserDTO meetingUnAssignUserDTO) {
        Meeting meeting = findById(meetingUnAssignUserDTO.getMeetingId());
        User user = userService.findUserById(meetingUnAssignUserDTO.getUserId());
        if (!(userService.isUserInMeeting(user.getUsername(), meeting.getId()))) {
            throw new UserIsAlreadyAssignedToMeetingException("User is not part of current meeting.");
        }
        meeting.getUsers().remove(user);
        meetingRepository.save(meeting);
        return Json.createObjectBuilder()
                .add("message", "User is successfully removed from meeting.")
                .build();
    }

    @Transactional
    public JsonObject addUserToMeeting(MeetingAssignUserDTO meetingAssignUserDTO) {
        User userToAssign = userService.findUserById(meetingAssignUserDTO.getUserId());
        Meeting meeting = findById(meetingAssignUserDTO.getMeetingId());
        List<User> users = userService.findUsersNotAssignedToMeeting(meeting.getId());
        if (!(projectService.isUserInProject(meeting.getProject().getKey(), userToAssign.getUsername()))) {
            throw new ValidationException("User is not in current project.");
        }
        if (meeting.getDate().isBefore(LocalDateTime.now())) {
            throw new MeetingOutdatedException("Meeting is outdated.");
        }
        meeting.getUsers().add(userToAssign);
        return Json.createObjectBuilder()
                .add("message", "User is added to meeting!")
                .build();
    }

    @Transactional
    public void delete(Long id) {
        Meeting currentMeeting = findById(id);
        currentMeeting.getUsers().clear();
        meetingRepository.save(currentMeeting);
        meetingRepository
                .delete(id);
    }

    public void deleteByUsername(String username) {
        List<Meeting> meetings = meetingRepository.findAllByUsername(username);
        meetings.forEach(meeting -> {
            meeting.getUsers().clear();
            meetingRepository.save(meeting);
        });
    }

    public void deleteRelatedToProjectMeetings(Long projectId) {
        meetingRepository.deleteByProjectId(projectId);
    }

}
