package org.cbg.projectmanagement.project_management.dto.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cbg.projectmanagement.project_management.dto.auth.AuthResponseDTO;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProjectResponseDTO {

    private String key;
    private String title;
}
