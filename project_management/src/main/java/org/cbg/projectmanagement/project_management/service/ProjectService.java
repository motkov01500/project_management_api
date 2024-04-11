package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.project.ProjectCheckForUserDTO;
import org.cbg.projectmanagement.project_management.dto.project.ProjectCreateDTO;
import org.cbg.projectmanagement.project_management.dto.project.ProjectUpdateDTO;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.repository.ProjectRepository;

import java.util.List;

@Stateless
public class ProjectService {

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private UserService userService;

    @Context
    private SecurityContext context;

    public List<Project> findAll() {
        return projectRepository
                .findAll();
    }

    public List<Project> findCurrentUserProjects() {
        return projectRepository
                .findProjectsRelatedToUser(context.getUserPrincipal().getName());
    }

    public Project findById(Long id) {
        return projectRepository
                .findById(id)
                .orElseThrow(()-> new NotFoundResourceException(Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(Json.createObjectBuilder()
                                .add("message","Project was not found")
                                .build())
                        .build()));
    }

    public List<Project> findUnassignedProjects() {
        return  projectRepository
                .findUnassignedProjects();
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
        if (!projectUpdateDTO.getKey().isEmpty() && !(projectUpdateDTO.getKey().equals(project.getKey()))) {
            project.setKey(projectUpdateDTO.getKey());
        }

        if (!projectUpdateDTO.getTitle().isEmpty() && !(projectUpdateDTO.getTitle().equals(project.getTitle()))) {
            project.setTitle(projectUpdateDTO.getTitle());
        }

        projectRepository.update(project);
        return project;
    }

    @Transactional
    public JsonObject assignUserToProject(Long userId, Long projectId) {
        User currentUser = userService.findUserById(userId);
        Project currentProject = findById(projectId);
        currentProject.addUser(currentUser);
        projectRepository.update(currentProject);
        return Json.createObjectBuilder()
                .add("message","User is successfully added to project")
                .build();
    }

    public void delete(Long id) {
        projectRepository.delete(id);
    }
}
