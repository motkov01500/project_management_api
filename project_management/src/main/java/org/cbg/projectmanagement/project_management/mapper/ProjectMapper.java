package org.cbg.projectmanagement.project_management.mapper;

import org.cbg.projectmanagement.project_management.dto.ProjectDTO;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jsr330")
public interface ProjectMapper {

    ProjectDTO mapProjectToProjectDTO(Project project);

    Project mapProjectDTOToProject(ProjectDTO projectDTO);
}
