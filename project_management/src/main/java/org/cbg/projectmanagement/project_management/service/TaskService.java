package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.task.TaskCreateDTO;
import org.cbg.projectmanagement.project_management.dto.task.TaskUpdateDTO;
import org.cbg.projectmanagement.project_management.dto.task.TaskUpdateProgressDTO;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.entity.Task;
import org.cbg.projectmanagement.project_management.entity.TaskStatus;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.repository.TaskRepository;

import java.util.List;

@Stateless
public class TaskService {

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private ProjectService projectService;

    @Inject
    private TaskStatusService taskStatusService;

    @Context
    private SecurityContext context;

    public List<Task> findAll() {
        return taskRepository
                .findAll();
    }

    public Task findById(Long id) {
        return taskRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Task was not found"));
    }

    @Transactional
    public List<Task> getTasksRelatedToCurrentUserProject() {
        return taskRepository
                .getTasksRelatedToUser(context.getUserPrincipal().getName());
    }

    @Transactional
    public Task create(TaskCreateDTO taskCreateDTO) {
        Project project = projectService.findById(taskCreateDTO.getProjectId());
        TaskStatus taskStatus = taskStatusService.findByName(taskCreateDTO.getStatusName());
        Task newTask = new Task(0,
                taskCreateDTO.getInitialEstimation(), 0, project, taskStatus);
        taskRepository.create(newTask);
        return newTask;
    }

    public Task updateProgressForUser(Long id, TaskUpdateProgressDTO taskUpdateProgressDTO) {
        Task currentTask = findById(id);
        currentTask = updateProgress(id, taskUpdateProgressDTO);
        return currentTask == null ? findById(id) : currentTask;
    }

    @Transactional
    public Task updateProgress(Long id, TaskUpdateProgressDTO taskUpdateProgressDTO) {
        Task currentTask = findById(id);
        currentTask.setProgress(taskUpdateProgressDTO.getProgress());
        currentTask.setTaskStatus(taskStatusService
                .findByName("DONE"));
        taskRepository.update(currentTask);
        return currentTask;
    }

    @Transactional
    public Task updateTask(Long id, TaskUpdateDTO taskUpdateDTO) {
        Task currentTask = findById(id);

        if (taskUpdateDTO.getProgress() != 0) {
            currentTask.setProgress(taskUpdateDTO.getProgress());
        }

        if (!(taskUpdateDTO.getStatusName().isEmpty())) {
            currentTask.setTaskStatus(taskStatusService.findByName(taskUpdateDTO.getStatusName()));
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
