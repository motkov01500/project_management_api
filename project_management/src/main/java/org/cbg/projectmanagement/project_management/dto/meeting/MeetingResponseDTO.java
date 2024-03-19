package org.cbg.projectmanagement.project_management.dto.meeting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cbg.projectmanagement.project_management.dto.project.ProjectResponseDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MeetingResponseDTO {

    private LocalDateTime date;
    private String status;
    private ProjectResponseDTO project;
}
