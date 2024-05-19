package org.cbg.projectmanagement.project_management.dto.project;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class ProjectAssignUserDTO {

    private List<Long> users;
    private Long projectId;
}
