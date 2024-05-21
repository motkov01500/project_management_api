package org.cbg.projectmanagement.project_management.dto.meeting;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class MeetingCreateDTO {

    private String date;
    private String title;
    private Long projectId;
}
