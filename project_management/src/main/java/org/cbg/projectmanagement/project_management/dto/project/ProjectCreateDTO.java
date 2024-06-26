package org.cbg.projectmanagement.project_management.dto.project;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ProjectCreateDTO {

    private String key;
    private String title;
}
