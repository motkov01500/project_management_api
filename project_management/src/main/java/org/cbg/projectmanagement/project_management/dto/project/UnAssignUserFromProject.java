package org.cbg.projectmanagement.project_management.dto.project;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UnAssignUserFromProject {

    private Long userId;
    private String projectKey;
}
