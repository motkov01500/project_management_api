package org.cbg.projectmanagement.project_management.dto.task;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AssignUserToTaskDTO {

    private List<Long> users;
    private Long taskId;
}
