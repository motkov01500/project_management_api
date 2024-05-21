package org.cbg.projectmanagement.project_management.mapper;

import org.cbg.projectmanagement.project_management.dto.meeting.MeetingResponseDTO;
import org.cbg.projectmanagement.project_management.dto.meeting.RecentMeetingDTO;
import org.cbg.projectmanagement.project_management.entity.Meeting;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jsr330")
public interface MeetingMapper {

    MeetingResponseDTO mapMeetingToMeetingDTO(Meeting meeting);

    RecentMeetingDTO mapMeetingToRecentMeetingDTO(Meeting meeting);
}
