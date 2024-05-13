package org.cbg.projectmanagement.project_management.dto.task;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AssignUserToTaskDTO {

    private String username;
    private Long taskId;
}
