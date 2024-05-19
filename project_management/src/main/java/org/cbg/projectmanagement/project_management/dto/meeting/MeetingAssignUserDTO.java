package org.cbg.projectmanagement.project_management.dto.meeting;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class MeetingAssignUserDTO {

    private List<Long> users;
    private Long meetingId;
}