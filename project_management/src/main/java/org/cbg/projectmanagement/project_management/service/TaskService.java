package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.task.AssignUserToTaskDTO;
import org.cbg.projectmanagement.project_management.dto.task.TaskCreateDTO;
import org.cbg.projectmanagement.project_management.dto.task.TaskUpdateDTO;
import org.cbg.projectmanagement.project_management.dto.task.TaskUpdateProgressDTO;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.entity.Task;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.enums.TaskStatus;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.exception.UserAlreadyAssignedToTaskException;
import org.cbg.projectmanagement.project_management.exception.ValidationException;
import org.cbg.projectmanagement.project_management.repository.TaskRepository;

import java.util.List;

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

    public List<Task> findAllRelatedToProject(String projectKey) {
        List<Task> tasks = taskRepository.getAllTasksRelatedToProject(projectKey);
        Project project = projectService.findByKey(projectKey);
        if(tasks.isEmpty()) {
            throw new NotFoundResourceException("Tasks are not found for current project.");
        }
        return taskRepository.getAllTasksRelatedToProject(projectKey);
    }

    public Task findById(Long id) {
        Task task = taskRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Task was not found"));
        if (task.getIsDeleted()) {
            throw new NotFoundResourceException("Task was not found");
        }
        return task;
    }

    @Transactional
    public List<Task> getTasksRelatedToCurrentUserAndProject(String projectKey) {
        Project project = projectService.findByKey(projectKey);
        return taskRepository
                .getTasksRelatedToCurrentUserAndProject(context.getUserPrincipal().getName(), projectKey);
    }

    @Transactional
    public JsonObject assignUserToTask(AssignUserToTaskDTO assignUserToTaskDTO) {
        User user = userService.getUserByUsername(assignUserToTaskDTO.getUsername());
        Task task = findById(assignUserToTaskDTO.getTaskId());
        if(isTaskInProject(assignUserToTaskDTO.getUsername(),assignUserToTaskDTO.getTaskId())) {
            throw new UserAlreadyAssignedToTaskException("User Assignment Error: The user is already assigned to this task. Please select a different user or review the task assignment.");
        }
        task.getUsers().add(user);
        taskRepository.update(task);
        return Json.createObjectBuilder()
                .add("message", "User is successfully assigned to task.")
                .build();
    }

    @Transactional
    public Task create(TaskCreateDTO taskCreateDTO) {
        Project project = projectService.findById(taskCreateDTO.getProjectId());
        Task newTask = new Task(0,
                taskCreateDTO.getInitialEstimation(), 0, project, TaskStatus.TODO.name(), Boolean.FALSE);
        taskRepository.create(newTask);
        return newTask;
    }

    @Transactional
    public Task updateProgress(Long id, TaskUpdateProgressDTO taskUpdateProgressDTO) {
        Task currentTask = findById(id);
        validateTaskProgress(taskUpdateProgressDTO.getProgress(), currentTask.getProgress());
        currentTask.setProgress(taskUpdateProgressDTO.getProgress());
        if (taskUpdateProgressDTO.getProgress() == 100) {
            currentTask.setTaskStatus(TaskStatus.DONE.name());
        }
        if(taskUpdateProgressDTO.getProgress() > 0 && taskUpdateProgressDTO.getProgress() < 100) {
            currentTask.setTaskStatus(TaskStatus.IN_PROGRESS.name());
        }
        taskRepository.update(currentTask);
        return currentTask;
    }

    @Transactional
    public Task updateTask(Long id, TaskUpdateDTO taskUpdateDTO) {
        Task currentTask = findById(id);
        validateTaskProgress(taskUpdateDTO.getProgress(), currentTask.getProgress());
        currentTask.setProgress(taskUpdateDTO.getProgress());
        validateSpentHours(taskUpdateDTO.getHoursSpent(), currentTask.getHoursSpent(),
                currentTask.getInitialEstimation());
        currentTask.setHoursSpent(taskUpdateDTO.getHoursSpent());
        taskRepository.update(currentTask);
        return currentTask;
    }

    public void delete(Long id) {
        taskRepository.delete(id);
    }

    public void deleteRelatedToProjectTasks(Long projectId) {
        taskRepository.deleteByProjectId(projectId);
    }

    private boolean isTaskInProject(String username, Long taskId) {
        return taskRepository.isUserAssignedAlreadyToTask(username, taskId);
    }

    private void validateTaskProgress(int newProjectProgress, int oldProjectProgress) {
        if (newProjectProgress > 100 || newProjectProgress < 0) {
            throw new ValidationException("Invalid value. The progress must be between 0 and 100");
        }
        if (newProjectProgress < oldProjectProgress) {
            throw new ValidationException("Invalid value. The new progress must be greater than old.");
        }
    }

    private void validateSpentHours(int newSpentHours, int oldSpentHours, int initialEstimation) {
        if (newSpentHours < oldSpentHours) {
            throw new ValidationException("Invalid value. New spent hours must be greater than already spent.");
        }
        if (newSpentHours > initialEstimation) {
            throw new ValidationException("Invalid value. New spent hours mustn't be greater than initial estimation hours.");
        }
    }
}
