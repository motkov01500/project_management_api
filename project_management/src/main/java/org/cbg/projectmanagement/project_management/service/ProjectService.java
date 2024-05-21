package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.Sort;
import org.cbg.projectmanagement.project_management.dto.project.*;
import org.cbg.projectmanagement.project_management.entity.*;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.exception.ProjectAlreadyExistsException;
import org.cbg.projectmanagement.project_management.exception.UserAlreadyInProjectException;
import org.cbg.projectmanagement.project_management.mapper.MeetingMapper;
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
    private MeetingMapper meetingMapper;

    @Inject
    private TaskService taskService;

    @Inject
    private ProjectMapper projectMapper;

    @Context
    private SecurityContext context;

    public List<Project> findAll(int page, int offset, Sort sort) {
        if(sort.getColumn().isEmpty()) {
            sort.setColumn("id");
        }
        List<Project> projects = projectRepository
                .findAll(page, offset, sort);
        return projects;
    }

    public List<Project> findAllWithoutPaging() {
        return projectRepository.findAll(0, 0, null);
    }

    public int findAllSize() {
        return projectRepository.findAll(0, 0, null).size();
    }

    public List<ProjectResponseDTO> findCurrentUserProjects(int pageNumber, int offside, Sort sort) {
        if (sort.getColumn().isEmpty()) {
            sort.setColumn("id");
        }
        List<Project> projects = projectRepository
                .findProjectsRelatedToUser(context.getUserPrincipal().getName(), pageNumber, offside, sort);
        List<ProjectResponseDTO> mappedProjects = projects
                .stream()
                .map(project -> projectMapper.mapProjectToProjectDTO(project))
                .collect(Collectors.toList());
        List<ProjectResponseDTO> finalMappedProjects = mappedProjects.stream().map(project -> {
                    List<Meeting> meeting = meetingService.findMostRecentMeetingToUser(project.getKey());
                    if (!meeting.isEmpty()) {
                        project.setMostRecentMeeting(meetingMapper.mapMeetingToRecentMeetingDTO(meeting.get(0)));
                    }
                    project.setRemainingTasks(taskService.countNotFinishedTaskToCurrentUser(project.getKey()));
                    return project;
                })
                .collect(Collectors.toList());
        return finalMappedProjects;
    }

    public int findCurrentUserProjectsSize() {
        return projectRepository
                .findProjectsRelatedToUser(context.getUserPrincipal().getName(), 0, 0, null).size();
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

    @Transactional
    public Project create(ProjectCreateDTO projectCreateDTO) {
        Project project = new Project(projectCreateDTO.getKey(), projectCreateDTO.getTitle(),
                Boolean.FALSE);
        if (projectRepository.isProjectExists(projectCreateDTO.getKey())) {
            throw new ProjectAlreadyExistsException("Project with this key already exists.");
        }
        projectRepository.save(project);
        return project;
    }

    @Transactional
    public Project update(Long id, ProjectUpdateDTO projectUpdateDTO) {
        Project project = findById(id);
        if (projectRepository.isProjectExists(projectUpdateDTO.getKey())) {
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
        Project currentProject = findById(projectAssignUserDTO.getProjectId());
        List<User> users = projectAssignUserDTO.getUsers()
                .stream()
                .map(user -> {
                    User currentUser = userService.findUserById(user);
                    if (isUserInProject(currentProject.getKey(), currentUser.getUsername())) {
                        throw new UserAlreadyInProjectException("Any of users you chose," +
                                " already in current project.");
                    }
                    return currentUser;
                })
                .collect(Collectors.toList());
        currentProject.getUsers().addAll(users);
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
        List<Project> projects = projectRepository.findProjectsRelatedToUser(username, 0, 0, null);
        User currentUser = userService.getUserByUsername(username);
        projects.forEach(project -> {
            project.getUsers().remove(currentUser);
            projectRepository.save(project);
        });
    }

    private void validateDeletedProject(Project project) {
        if (project.getIsDeleted()) {
            throw new NotFoundResourceException("Project was not found.");
        }
    }
}