package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.project.*;
import org.cbg.projectmanagement.project_management.dto.task.UnAssignUserToTaskDTO;
import org.cbg.projectmanagement.project_management.entity.*;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.exception.ProjectAlreadyExistsException;
import org.cbg.projectmanagement.project_management.exception.UserAlreadyInProjectException;
import org.cbg.projectmanagement.project_management.mapper.ProjectMapper;
import org.cbg.projectmanagement.project_management.repository.ProjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class ProjectService {

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private UserService userService;

    @Inject
    private MeetingService meetingService;

    @Inject
    private TaskService taskService;

    @Inject
    private ProjectMapper projectMapper;

    @Context
    private SecurityContext context;

    public List<Project> findAll(int page, int offset) {
        List<Project> projects = projectRepository
                .findAll(page, offset);
        return projects;
    }

    public List<Project> findAllWithoutPaging() {
        return projectRepository.findAll(0, 0);
    }

    public int findAllSize() {
        return projectRepository.findAll(0, 0).size();
    }

    public List<Project> findCurrentUserProjects(int pageNumber, int offside) {
        return projectRepository
                .findProjectsRelatedToUser(context.getUserPrincipal().getName(), pageNumber, offside);
    }

    public int findCurrentUserProjectsSize() {
        return projectRepository
                .findProjectsRelatedToUser(context.getUserPrincipal().getName(), 0, 0).size();
    }

    public Project findById(Long id) {
        Project project = projectRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Project was not found"));
        validateDeletedProject(project);
        return project;
    }

    public Project findByKey(String projectKey) {
        Project project = projectRepository
                .findProjectByKey(projectKey)
                .orElseThrow(() -> new NotFoundResourceException("Project was not found"));
        validateDeletedProject(project);
        return project;
    }

    public boolean isUserInProject(String projectKey, String username) {
        return projectRepository
                .isUserInProject(projectKey, username);
    }

    public List<Project> findUnassignedProjects(int pageNumber, int offset) {
        return projectRepository
                .findUnassignedProjects(pageNumber, offset);
    }

    public int findUnassignedProjectsSize() {
        return projectRepository
                .findUnassignedProjects(0, 0)
                .size();
    }

    @Transactional
    public Project create(ProjectCreateDTO projectCreateDTO) {
        Project project = new Project(projectCreateDTO.getKey(), projectCreateDTO.getTitle(),
                Boolean.FALSE);
        if(projectRepository.isProjectExists(projectCreateDTO.getKey())) {
            throw new ProjectAlreadyExistsException("Project with this key already exists.");
        }
        projectRepository.save(project);
        return project;
    }

    @Transactional
    public Project update(Long id, ProjectUpdateDTO projectUpdateDTO) {
        Project project = findById(id);
        if(projectRepository.isProjectExists(projectUpdateDTO.getKey())) {
            throw new ProjectAlreadyExistsException("Project with this key already exists");
        }
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
    public JsonObject removeUserFromProject(UnAssignUserFromProject unAssignUserFromProject) {
        Project project = findByKey(unAssignUserFromProject.getProjectKey());
        User user = userService.findUserById(unAssignUserFromProject.getUserId());
        if (!(isUserInProject(project.getKey(), user.getUsername()))) {
            throw new NotFoundResourceException("User is not assigned to current project");
        }
        List<Meeting> meetingsRelatedToProjectUsers = meetingService
                .findMeetingsRelatedToUserAndProject(project.getKey(), user.getUsername());
        List<Task> tasksRelatedToProjectUsers = taskService
                .getTasksRelatedToUserAndProject(project.getKey(), user.getUsername());
        meetingsRelatedToProjectUsers.forEach(meeting -> {
            meeting.getUsers().remove(user);
            meetingService.defaultUpdate(meeting.getId(), meeting);
        });
        tasksRelatedToProjectUsers.forEach(task -> {
            task.getUsers().remove(user);
            taskService.defaultUpdate(task.getId(), task);
        });
        project.getUsers().remove(user);
        projectRepository.update(project);
        return Json.createObjectBuilder()
                .add("message", "User is successfully removed from project.")
                .build();
    }

    @Transactional
    public JsonObject assignUserToProject(ProjectAssignUserDTO projectAssignUserDTO) {
        User currentUser = userService.findUserById(projectAssignUserDTO.getUserId());
        Project currentProject = findById(projectAssignUserDTO.getProjectId());
        if (isUserInProject(currentProject.getKey(), currentUser.getUsername())) {
            throw new UserAlreadyInProjectException("User already in current project");
        }
        currentProject.getUsers().add(currentUser);
        projectRepository.update(currentProject);
        projectRepository.update(currentProject);
        return Json.createObjectBuilder()
                .add("message", "User is successfully assigned to project.")
                .build();
    }

    @Transactional
    public void delete(Long id) {
        meetingService.deleteRelatedToProjectMeetings(id);
        taskService.deleteRelatedToProjectTasks(id);
        projectRepository.delete(id);
    }

    public void deletedProjectsByUsername(String username) {
        List<Project> projects = projectRepository.findProjectsRelatedToUser(username,0,0);
        projects.forEach(project -> {
            project.getUsers().clear();
            projectRepository.save(project);
        });
    }

    private void validateDeletedProject(Project project) {
        if (project.getIsDeleted()) {
            throw new NotFoundResourceException("Project was not found.");
        }
    }
}