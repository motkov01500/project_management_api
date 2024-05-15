package org.cbg.projectmanagement.project_management.dto.meeting;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cbg.projectmanagement.project_management.dto.project.ProjectResponseDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class MeetingResponseDTO {

    private Long id;
    private LocalDateTime date;
    private Boolean isUsersAvailable;
    private String title;
    private Boolean isDeleted;
    private ProjectResponseDTO project;
}
