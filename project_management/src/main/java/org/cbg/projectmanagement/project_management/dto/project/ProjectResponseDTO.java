package org.cbg.projectmanagement.project_management.dto.project;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cbg.projectmanagement.project_management.dto.auth.AuthResponseDTO;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ProjectResponseDTO {

    private Long id;
    private String key;
    private String title;
}
