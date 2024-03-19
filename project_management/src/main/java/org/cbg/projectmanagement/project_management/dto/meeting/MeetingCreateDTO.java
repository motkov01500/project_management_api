package org.cbg.projectmanagement.project_management.dto.meeting;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MeetingCreateDTO {

    private LocalDateTime date;
    private Long projectId;
}
