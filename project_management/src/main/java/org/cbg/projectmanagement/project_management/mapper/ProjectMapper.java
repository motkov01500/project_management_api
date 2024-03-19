package org.cbg.projectmanagement.project_management.mapper;

import org.cbg.projectmanagement.project_management.dto.project.ProjectResponseDTO;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jsr330")
public interface ProjectMapper {

    ProjectResponseDTO mapProjectToProjectDTO(Project project);
}
