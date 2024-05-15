package org.cbg.projectmanagement.project_management.dto.task;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UnAssignUserToTaskDTO {

    private Long taskId;
    private Long userId;
}
