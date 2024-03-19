package org.cbg.projectmanagement.project_management.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingCreateDTO;
import org.cbg.projectmanagement.project_management.dto.meeting.MeetingUpdateDTO;
import org.cbg.projectmanagement.project_management.entity.Meeting;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.mapper.MeetingMapper;
import org.cbg.projectmanagement.project_management.repository.MeetingRepository;

import java.util.List;

@Named("MeetingService")
public class MeetingService {

    @Inject
    MeetingRepository meetingRepository;

    @Inject
    MeetingMapper meetingMapper;

    @Inject
    ProjectService projectService;

    public List<Meeting> findAll() {
        return meetingRepository
                .findAll();
    }

    public Meeting findById(Long id) {
        return meetingRepository
                .findById(id);
    }

    @Transactional
    public  Meeting create(MeetingCreateDTO meetingCreateDTO) {
        Project project = projectService.findById(meetingCreateDTO.getProjectId());
        Meeting newMeeting = new Meeting(meetingCreateDTO.getDate(), "UPCOMING", project);
        meetingRepository
                .create(newMeeting);
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

    public void delete(Long id) {
        meetingRepository
                .delete(id);
    }
}
