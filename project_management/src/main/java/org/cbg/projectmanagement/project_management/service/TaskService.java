package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.task.TaskCreateDTO;
import org.cbg.projectmanagement.project_management.dto.task.TaskUpdateDTO;
import org.cbg.projectmanagement.project_management.dto.task.TaskUpdateProgressDTO;
import org.cbg.projectmanagement.project_management.entity.Meeting;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.entity.Task;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Stateless
public class TaskService {

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private ProjectService projectService;

    @Inject
    private UserService userService;

    @Context
    private SecurityContext context;

    public List<Task> findAll() {
        return taskRepository
                .findAll();
    }

    public Task findById(Long id) {
        return taskRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundResourceException(Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(Json.createObjectBuilder()
                                .add("message", "Task was not found")
                                .build())
                        .build()));
    }

    @Transactional
    public List<Task> getTasksRelatedToCurrentUserProject() {
        return taskRepository
                .getTasksRelatedToUser(context.getUserPrincipal().getName());
    }

    @Transactional
    public Task create(TaskCreateDTO taskCreateDTO) {
        Project project = projectService.findById(taskCreateDTO.getProjectId());
        Task newTask = new Task(0, taskCreateDTO.getStatus(),
                taskCreateDTO.getInitialEstimation(), 0, project);
        taskRepository.create(newTask);
        return newTask;
    }

    public Task updateProgressForUser(Long id, TaskUpdateProgressDTO taskUpdateProgressDTO) {
        Task currentTask = findById(id);
        currentTask = updateProgress(id, taskUpdateProgressDTO);
        return currentTask == null ? findById(id) : currentTask;
    }

    public Task updateProgress(Long id, TaskUpdateProgressDTO taskUpdateProgressDTO) {
        Task currentTask = findById(id);
        currentTask.setProgress(taskUpdateProgressDTO.getProgress());
        if (taskUpdateProgressDTO.getProgress() == 100) {
            currentTask.setStatus("DONE");
        }
        taskRepository.update(currentTask);
        return currentTask;
    }

    public Task updateTask(Long id, TaskUpdateDTO taskUpdateDTO) {
        Task currentTask = findById(id);

        if (taskUpdateDTO.getProgress() != 0) {
            currentTask.setProgress(taskUpdateDTO.getProgress());
        }

        if (!(taskUpdateDTO.getStatus().isEmpty())
                && !(taskUpdateDTO.getStatus().equals(currentTask.getStatus()))) {
            currentTask.setStatus(taskUpdateDTO.getStatus());
        }

        if (taskUpdateDTO.getHoursSpent() > currentTask.getHoursSpent()) {
            currentTask.setHoursSpent(taskUpdateDTO.getHoursSpent());
        }

        taskRepository.update(currentTask);
        return currentTask;
    }

    public void delete(Long id) {
        taskRepository.delete(id);
    }
}
