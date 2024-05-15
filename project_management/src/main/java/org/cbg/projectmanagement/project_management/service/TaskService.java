package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.task.*;
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

    public List<Task> findAll(int page, int offset) {
        List<Task> tasks = taskRepository
                .findAll(page, offset);
        return tasks;
    }

    public int findAllSize() {
        return taskRepository
                .findAll(0,0)
                .size();
    }

    public List<Task> findAllRelatedToProject(String projectKey, int page, int offside) {
        List<Task> tasks = taskRepository.getAllTasksRelatedToProject(projectKey, page, offside);
        Project project = projectService.findByKey(projectKey);
        if (tasks.isEmpty()) {
            throw new NotFoundResourceException("Tasks are not found for current project.");
        }
        return tasks;
    }

    public int findAllRelatedToProjectSize(String projectKey) {
        return taskRepository
                .getAllTasksRelatedToProject(projectKey ,0,0)
                .size();
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
    public List<Task> getTasksRelatedToCurrentUserAndProject(String projectKey, int page, int offside) {
        Project project = projectService.findByKey(projectKey);
        return taskRepository
                .getTasksRelatedToCurrentUserAndProject(context.getUserPrincipal().getName(), projectKey, page, offside);
    }

    public int getTasksRelatedToCurrentUserAndProjectSize(String projectKey) {
        return taskRepository
                .getTasksRelatedToCurrentUserAndProject(context.getUserPrincipal().getName(), projectKey,0,0)
                .size();
    }

    @Transactional
    public List<Task> getTasksRelatedToUserAndProject(String projectKey, String username) {
        Project project = projectService.findByKey(projectKey);
        return taskRepository
                .getTasksRelatedToCurrentUserAndProject(username, projectKey, 0, 0);
    }

    @Transactional
    public JsonObject removeUserFromTask(UnAssignUserToTaskDTO unAssignUserToTaskDTO) {
        Task task = findById(unAssignUserToTaskDTO.getTaskId());
        User user = userService.findUserById(unAssignUserToTaskDTO.getUserId());
        if (!(isUserInTask(unAssignUserToTaskDTO.getTaskId(), unAssignUserToTaskDTO.getUserId()))) {
            throw new NotFoundResourceException("User is not part of task.");
        }
        task.setIsUsersAvailable(Boolean.TRUE);
        task.getUsers().remove(user);
        taskRepository.save(task);
        return Json.createObjectBuilder()
                .add("message", "User is successfully removed from task.")
                .build();
    }

    @Transactional
    public JsonObject assignUserToTask(AssignUserToTaskDTO assignUserToTaskDTO) {
        User user = userService.getUserByUsername(assignUserToTaskDTO.getUsername());
        Task task = findById(assignUserToTaskDTO.getTaskId());
        List<User> users = userService.findUsersNotAssignedToTask(task.getId());
        if (isTaskInProject(assignUserToTaskDTO.getUsername(), assignUserToTaskDTO.getTaskId())) {
            throw new UserAlreadyAssignedToTaskException("User Assignment Error: The user is already assigned to this task. Please select a different user or review the task assignment.");
        }
        if (users.stream().count() == 1) {
            task.setIsUsersAvailable(Boolean.FALSE);
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
        if (taskCreateDTO.getTitle().isEmpty() || taskCreateDTO.getInitialEstimation() == 0) {
            throw new ValidationException("You must enter the title, initial estimation and project.");
        }
        newTask.setTitle(taskCreateDTO.getTitle());
        taskRepository.save(newTask);
        return newTask;
    }

    @Transactional
    public boolean isUserInTask(Long taskId, Long userId) {
        return taskRepository
                .isUserInTask(userId, taskId);
    }

    @Transactional
    public Task updateProgress(Long id, TaskUpdateProgressDTO taskUpdateProgressDTO) {
        Task currentTask = findById(id);
        validateTaskProgress(taskUpdateProgressDTO.getProgress(), currentTask.getProgress());
        currentTask.setProgress(taskUpdateProgressDTO.getProgress());
        if (taskUpdateProgressDTO.getProgress() == 100) {
            currentTask.setTaskStatus(TaskStatus.DONE.name());
        }
        if (taskUpdateProgressDTO.getProgress() > 0 && taskUpdateProgressDTO.getProgress() < 100) {
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
        if (!(taskUpdateDTO.getTitle().isEmpty())) {
            currentTask.setTitle(taskUpdateDTO.getTitle());
        }
        taskRepository.update(currentTask);
        return currentTask;
    }

    public void defaultUpdate(Long id, Task newTask) {
        Task currentTask = findById(id);
        currentTask = newTask;
        taskRepository.update(currentTask);
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
