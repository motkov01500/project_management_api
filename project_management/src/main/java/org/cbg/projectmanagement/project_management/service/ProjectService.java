package org.cbg.projectmanagement.project_management.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.project.ProjectCheckForUserDTO;
import org.cbg.projectmanagement.project_management.dto.project.ProjectCreateDTO;
import org.cbg.projectmanagement.project_management.dto.project.ProjectUpdateDTO;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.repository.ProjectRepository;

import java.util.List;

@Named("ProjectService")
public class ProjectService {

    @Inject
    ProjectRepository projectRepository;

    @Context
    SecurityContext context;

    public List<Project> findAll() {
        return projectRepository
                .findAll();
    }

    public Project findById(Long id) {
        return projectRepository
                .findById(id);
    }

    public boolean isUserInProject(String key) {
        boolean tst = projectRepository
                .findUserInProject(key, context.getUserPrincipal().getName());
        return tst;
    }

    @Transactional
    public Project create(ProjectCreateDTO projectCreateDTO) {
        Project project = new Project(projectCreateDTO.getKey(), projectCreateDTO.getTitle());
        projectRepository.create(project);
        return project;
    }

    @Transactional
    public Project update(Long id, ProjectUpdateDTO projectUpdateDTO) {
        Project project = findById(id);
        if (!projectUpdateDTO.getKey().isEmpty()) {
            project.setKey(projectUpdateDTO.getKey());
        }

        if (!projectUpdateDTO.getTitle().isEmpty()) {
            project.setTitle(projectUpdateDTO.getTitle());
        }

        return project;
    }

    public void delete(Long id) {
        projectRepository.delete(id);
    }
}
