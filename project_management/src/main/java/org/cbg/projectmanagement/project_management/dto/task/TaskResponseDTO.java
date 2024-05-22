package org.cbg.projectmanagement.project_management.dto.task;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cbg.projectmanagement.project_management.dto.project.ProjectResponseDTO;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class TaskResponseDTO {

    private int id;
    private String title;
    private int progress;
    private int initialEstimation;
    private int usersAvailable;
    private int hoursSpent;
    private String taskStatus;
    private Boolean isDeleted;
    private ProjectResponseDTO project;
}
