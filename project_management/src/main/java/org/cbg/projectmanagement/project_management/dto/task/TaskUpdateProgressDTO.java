package org.cbg.projectmanagement.project_management.dto.task;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class TaskUpdateProgressDTO {

    private int progress;
    private int hoursSpent;
}
