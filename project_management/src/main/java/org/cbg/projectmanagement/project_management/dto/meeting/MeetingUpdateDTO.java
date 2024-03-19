package org.cbg.projectmanagement.project_management.dto.meeting;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MeetingUpdateDTO {

    private LocalDateTime date;
    private String status;
}
