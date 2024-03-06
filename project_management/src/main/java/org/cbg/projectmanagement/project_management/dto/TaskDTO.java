package org.cbg.projectmanagement.project_management.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskDTO {

    private int progress;
    private String status;
    private int initialEstimation;
    private int hoursSpent;
}
