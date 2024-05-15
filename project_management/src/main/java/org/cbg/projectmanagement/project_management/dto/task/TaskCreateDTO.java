package org.cbg.projectmanagement.project_management.dto.task;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.cbg.projectmanagement.project_management.enums.TaskStatus;

@Getter
@Setter
@EqualsAndHashCode
public class TaskCreateDTO {

    private String title;
    private int initialEstimation;
    private Long projectId;
}
