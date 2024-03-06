package org.cbg.projectmanagement.project_management.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.cbg.projectmanagement.project_management.dto.MeetingDTO;
import org.cbg.projectmanagement.project_management.entity.Meeting;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.enumeration.MeetingStatus;
import org.cbg.projectmanagement.project_management.mapper.MeetingMapper;
import org.cbg.projectmanagement.project_management.repository.MeetingRepository;
import org.cbg.projectmanagement.project_management.request.MeetingDateUpdateRequest;
import org.cbg.projectmanagement.project_management.request.MeetingRequest;
import org.cbg.projectmanagement.project_management.request.MeetingStatusChangeRequest;

import java.util.List;
import java.util.stream.Collectors;

@Named("MeetingService")
public class MeetingService {

    @Inject
    MeetingRepository meetingRepository;

    @Inject
    MeetingMapper meetingMapper;

    @Inject
    ProjectService projectService;

    public List<MeetingDTO> findAll() {
        return meetingRepository.findAll()
                .stream()
                .map(meetingMapper::mapMeetingToMeetingDTO)
                .collect(Collectors.toList());
    }


    public Meeting findById(Long id) {
        return meetingRepository
                .findById(id);
    }

    @Transactional
    public  Meeting create(MeetingRequest request) {
        Project project = projectService.findById(request.getProjectId());
        Meeting newMeeting = new Meeting(request.getDate(), MeetingStatus.UPCOMING.getName(), project);
        meetingRepository.create(newMeeting);
        return newMeeting;
    }

    public Meeting updateStatus(Long id, MeetingStatusChangeRequest request) {
        Meeting updatedMeeting = findById(id);
        updatedMeeting.setStatus(request.getStatus());
        meetingRepository.update(updatedMeeting);
        return updatedMeeting;
    }

    public Meeting updateDate(Long id, MeetingDateUpdateRequest request) {
        Meeting updatedMeeting = findById(id);
        updatedMeeting.setDate(request.getDate());
        meetingRepository.update(updatedMeeting);
        return updatedMeeting;
    }

    public void delete(Long id) {
        meetingRepository.delete(id);
    }
}
