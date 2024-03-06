package org.cbg.projectmanagement.project_management.mapper;

import org.cbg.projectmanagement.project_management.dto.MeetingDTO;
import org.cbg.projectmanagement.project_management.entity.Meeting;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jsr330")
public interface MeetingMapper {

    MeetingDTO mapMeetingToMeetingDTO(Meeting meeting);

    Meeting mapMeetingDTOToMeeting(MeetingDTO meeting);
}
