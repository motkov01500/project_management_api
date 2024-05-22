package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.meeting.*;
import org.cbg.projectmanagement.project_management.entity.Meeting;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.exception.MeetingOutdatedException;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.exception.UserIsAlreadyAssignedToMeetingException;
import org.cbg.projectmanagement.project_management.exception.ValidationException;
import org.cbg.projectmanagement.project_management.mapper.MeetingMapper;
import org.cbg.projectmanagement.project_management.repository.MeetingRepository;
import org.cbg.projectmanagement.project_management.dto.Sort;
import org.cbg.projectmanagement.project_management.enums.SortOrder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Meeting> findMostRecentMeetingToUser(String projectKey) {
        return meetingRepository
                .findRecentMeetingsToUser(context.getUserPrincipal().getName(), projectKey);
    }

    public List<Meeting> getAllMeetingsToCurrentUser(int pageNumber, int offsite, Sort sort) {
        if(sort.getOrder() == null || sort.getColumn().isEmpty()) {
            sort.setOrder(SortOrder.DEFAULT);
            sort.setColumn("id");
        }
        List<Meeting> meetings = meetingRepository
                .getCurrentUserMeetings(context.getUserPrincipal().getName(), pageNumber, offsite, sort);
        return meetings;
    }

    public int getAllMeetingsToCurrentUserSize() {
        return meetingRepository
                .getCurrentUserMeetings(context.getUserPrincipal().getName(), 0, 0, null)
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

    public MeetingResponseDTO findByIdDTO(Long id) {
        Meeting meeting = meetingRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Meeting was not found"));
        if (meeting.getIsDeleted()) {
            throw new NotFoundResourceException("Meeting was not found");
        }
        MeetingResponseDTO meetingResponseDTO = meetingMapper.mapMeetingToMeetingDTO(meeting);
        meetingResponseDTO.setUsersAvailable(userService.findUsersNotAssignedToMeetingSize(meeting.getId()));
        return meetingResponseDTO;
    }

    public List<Meeting> findMeetingRelatedToProject(String projectKey, int pageNumber, int offset, Sort sort) {
        Project project = projectService.findByKey(projectKey);
        if(sort.getColumn().isEmpty()) {
            sort.setColumn("id");
        }
        List<Meeting> meetings = meetingRepository
                .getMeetingsRelatedToProjectWithPaging(projectKey, pageNumber, offset, sort);
        if (meetings.isEmpty()) {
            throw new NotFoundResourceException("Meetings are not found for current project.");
        }
        return meetings;
    }

    public int findMeetingRelatedToProjectSize(String projectKey) {
        Project project = projectService.findByKey(projectKey);
        List<Meeting> meetings = meetingRepository
                .getMeetingsRelatedToProjectWithPaging(projectKey, 0, 0, null);
        return meetings.size();
    }

    public List<Meeting> findMeetingsRelatedToCurrentUserAndProject(String projectKey, int page, int offset, Sort sort) {
        if(sort.getColumn().isEmpty()) {
            sort.setColumn("id");
        }
        List<Meeting> meetings = meetingRepository
                .getMeetingsRelatedToProjectAndUser(context.getUserPrincipal().getName(), projectKey, page, offset, sort);
        return meetings;
    }

    public int findMeetingsRelatedToCurrentUserAndProjectSize(String projectKey) {
        return meetingRepository
                .getMeetingsRelatedToProjectAndUser(context.getUserPrincipal().getName(), projectKey, 0, 0, null)
                .size();
    }

    public List<Meeting> findMeetingsRelatedToUserAndProject(String projectKey, String username) {
        return meetingRepository
                .getMeetingsRelatedToProjectAndUser(username, projectKey, 0, 0, null);
    }

    public boolean isUserAssignedToMeeting(Long meetingId, Long userId) {
        return meetingRepository.isUserInMeeting(meetingId, userId);
    }

    @Transactional
    public Meeting create(MeetingCreateDTO meetingCreateDTO) {
        Meeting meeting = new Meeting();
        meeting.setTitle(meetingCreateDTO.getTitle());
        Project project = projectService.findById(meetingCreateDTO.getProjectId());
        if (!meetingCreateDTO.getDate().isEmpty()) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime localDateTime = LocalDateTime.parse(meetingCreateDTO.getDate(), dateTimeFormatter);
            LocalDateTime newLocalDateTime = localDateTime.plusHours(3);
            meeting.setDate(newLocalDateTime);
        }
        meeting.setProject(project);
        meeting.setIsDeleted(Boolean.FALSE);
        if (!meetingCreateDTO.getTitle().isEmpty()) {
            meeting.setTitle(meetingCreateDTO.getTitle());
        }
        meetingRepository.save(meeting);
        return meeting;
    }

    public Meeting update(Long id, MeetingUpdateDTO meetingUpdateDTO) {
        Meeting updatedMeeting = findById(id);
        if (!meetingUpdateDTO.getDate().isEmpty()) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime localDateTime = LocalDateTime.parse(meetingUpdateDTO.getDate(), dateTimeFormatter);
            LocalDateTime newDateTime = localDateTime.plusHours(3);
            updatedMeeting.setDate(newDateTime);
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
        Meeting meeting = findById(meetingAssignUserDTO.getMeetingId());
        List<User> usersToAssign = meetingAssignUserDTO.getUsers()
                .stream()
                .map(user -> {
                    User userToAssign = userService.findUserById(user);
                    if (!(projectService.isUserInProject(meeting.getProject().getKey(), userToAssign.getUsername()))) {
                        throw new ValidationException("User is not in current project.");
                    }
                    if (isUserAssignedToMeeting(meeting.getId(), user)) {
                        throw new UserIsAlreadyAssignedToMeetingException("User is already assigned to this meeting.");
                    }
                    return userToAssign;
                })
                .collect(Collectors.toList());
        LocalDateTime newLocalDateTime = meeting.getDate().plusHours(3);
        if (newLocalDateTime.isBefore(LocalDateTime.now())) {
            throw new MeetingOutdatedException("Meeting is outdated.");
        }
        meeting.getUsers().addAll(usersToAssign);
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
        User currentUser = userService.getUserByUsername(username);
        meetings.forEach(meeting -> {
            meeting.getUsers().remove(currentUser);
            meetingRepository.save(meeting);
        });
    }

    public void deleteRelatedToProjectMeetings(Long projectId) {
        meetingRepository.deleteByProjectId(projectId);
    }

}
