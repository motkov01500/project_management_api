package org.cbg.projectmanagement.project_management.dto.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskResponseDTO {

    private int progress;
    private String status;
    private int initialEstimation;
    private int hoursSpent;
}
