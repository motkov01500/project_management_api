package org.cbg.projectmanagement.project_management.dto.task;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cbg.projectmanagement.project_management.dto.project.ProjectResponseDTO;
import org.cbg.projectmanagement.project_management.dto.task_status.TaskStatusResponseDTO;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class TaskResponseDTO {

    private int id;
    private int progress;
    private int initialEstimation;
    private int hoursSpent;
    private ProjectResponseDTO project;
    private TaskStatusResponseDTO taskStatus;
}
