package org.cbg.projectmanagement.project_management.dto.meeting;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class MeetingAssignUserDTO {

    private Long userId;
    private Long meetingId;
}
