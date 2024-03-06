package org.cbg.projectmanagement.project_management.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.cbg.projectmanagement.project_management.dto.ProjectDTO;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.mapper.ProjectMapper;
import org.cbg.projectmanagement.project_management.repository.ProjectRepository;
import org.cbg.projectmanagement.project_management.request.ProjectRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named("ProjectService")
public class ProjectService {

    @Inject
    ProjectRepository projectRepository;

    @Inject
    ProjectMapper projectMapper;

    public List<ProjectDTO> findAll() {
        return projectRepository
                .findAll()
                .stream()
                .map(projectMapper::mapProjectToProjectDTO)
                .collect(Collectors.toList());
    }

    public Project findById(Long id) {
        return projectRepository
                .findById(id);
    }

    @Transactional
    public ProjectDTO create(ProjectRequest request) {
        Project project = new Project(request.getKey(), request.getTitle());
        projectRepository.create(project);
        return projectMapper.mapProjectToProjectDTO(project);
    }

    @Transactional
    public ProjectDTO update(Long id, ProjectRequest request) {
        Project project = findById(id);
        if (!request.getKey().isBlank()) {
            project.setKey(request.getKey());
        }

        if (!request.getTitle().isBlank()) {
            project.setTitle(request.getTitle());
        }

        return projectMapper
                .mapProjectToProjectDTO(project);
    }

    public void delete(Long id) {
        projectRepository.delete(id);
    }
}
