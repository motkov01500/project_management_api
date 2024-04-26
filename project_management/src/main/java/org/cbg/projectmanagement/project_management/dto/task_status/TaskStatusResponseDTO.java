package org.cbg.projectmanagement.project_management.dto.task_status;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class TaskStatusResponseDTO {

    private Long id;
    private String name;
}
