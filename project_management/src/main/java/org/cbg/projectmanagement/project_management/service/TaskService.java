package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.Sort;
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
import java.util.stream.Collectors;

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

    public List<Task> findAll(int page, int offset, Sort sort) {
        if(sort.getColumn().isEmpty()) {
            sort.setColumn("id");
        }
        List<Task> tasks = taskRepository
                .findAll(page, offset, sort);
        return tasks;
    }

    public int findAllSize() {
        return taskRepository
                .findAll(0, 0, null)
                .size();
    }

    public List<Task> findAllRelatedToProject(String projectKey, int page, int offside, Sort sort) {
        if(sort.getColumn().isEmpty()) {
            sort.setColumn("id");
        }
        List<Task> tasks = taskRepository.getAllTasksRelatedToProject(projectKey, page, offside, sort);
        Project project = projectService.findByKey(projectKey);
        if (tasks.isEmpty()) {
            throw new NotFoundResourceException("Tasks are not found for current project.");
        }
        return tasks;
    }

    public int countNotFinishedTaskToCurrentUser(String projectKey) {
        return taskRepository
                .countNotFinishedTasksRelatedToUserAndProject(context.getUserPrincipal().getName(), projectKey);
    }

    public int findAllRelatedToProjectSize(String projectKey) {
        return taskRepository
                .getAllTasksRelatedToProject(projectKey, 0, 0, null)
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
    public List<Task> getTasksRelatedToCurrentUserAndProject(String projectKey, int page, int offside, Sort sort) {
        if(sort.getColumn().isEmpty()) {
            sort.setColumn("id");
        }
        Project project = projectService.findByKey(projectKey);
        return taskRepository
                .getTasksRelatedToCurrentUserAndProject(context.getUserPrincipal().getName(), projectKey, page, offside, sort);
    }

    public int getTasksRelatedToCurrentUserAndProjectSize(String projectKey) {
        return taskRepository
                .getTasksRelatedToCurrentUserAndProject(context.getUserPrincipal().getName(), projectKey, 0, 0, null)
                .size();
    }

    @Transactional
    public List<Task> getTasksRelatedToUserAndProject(String projectKey, String username) {
        Project project = projectService.findByKey(projectKey);
        return taskRepository
                .getTasksRelatedToCurrentUserAndProject(username, projectKey, 0, 0, null);
    }

    @Transactional
    public JsonObject removeUserFromTask(UnAssignUserToTaskDTO unAssignUserToTaskDTO) {
        Task task = findById(unAssignUserToTaskDTO.getTaskId());
        User user = userService.findUserById(unAssignUserToTaskDTO.getUserId());
        if (!(isUserInTask(unAssignUserToTaskDTO.getTaskId(), unAssignUserToTaskDTO.getUserId()))) {
            throw new NotFoundResourceException("User is not part of task.");
        }
        task.getUsers().remove(user);
        taskRepository.save(task);
        return Json.createObjectBuilder()
                .add("message", "User is successfully removed from task.")
                .build();
    }

    @Transactional
    public JsonObject assignUserToTask(AssignUserToTaskDTO assignUserToTaskDTO) {
        Task task = findById(assignUserToTaskDTO.getTaskId());
        Project project = projectService.findByKey(task.getProject().getKey());
        List<User> users = assignUserToTaskDTO.getUsers()
                .stream()
                .map(user -> {
                    User currentUser = userService.findUserById(user);
                    if (isTaskInProject(user, assignUserToTaskDTO.getTaskId())) {
                        throw new UserAlreadyAssignedToTaskException("Any of chosen users are in current task.");
                    }
                    if(!projectService.isUserInProject(project.getKey(),currentUser.getUsername())) {
                        throw new ValidationException("Any of chosen users are not in current project.");
                    }
                    return currentUser;
                })
                .collect(Collectors.toList());
        task.getUsers().addAll(users);
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
        validateSpentHours(taskUpdateProgressDTO.getHoursSpent(), currentTask.getHoursSpent());
        currentTask.setHoursSpent(taskUpdateProgressDTO.getHoursSpent());
        taskRepository.update(currentTask);
        return currentTask;
    }

    @Transactional
    public Task updateTask(Long id, TaskUpdateDTO taskUpdateDTO) {
        Task currentTask = findById(id);
        validateTaskProgress(taskUpdateDTO.getProgress(), currentTask.getProgress());
        currentTask.setProgress(taskUpdateDTO.getProgress());
        if (taskUpdateDTO.getProgress() == 100) {
            currentTask.setTaskStatus(TaskStatus.DONE.name());
        }
        if (taskUpdateDTO.getProgress() > 0 && taskUpdateDTO.getProgress() < 100) {
            currentTask.setTaskStatus(TaskStatus.IN_PROGRESS.name());
        }
        if(taskUpdateDTO.getHoursSpent()!= 0) {
            validateSpentHours(taskUpdateDTO.getHoursSpent(), currentTask.getHoursSpent());
            currentTask.setHoursSpent(taskUpdateDTO.getHoursSpent());
        }
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
        Task currentTask = findById(id);
        currentTask.getUsers().clear();
        taskRepository.update(currentTask);
        taskRepository.delete(id);
    }

    public void deleteRelatedToProjectTasks(Long projectId) {
        taskRepository.deleteByProjectId(projectId);
    }

    public void deleteTasksToUser(String username) {
        List<Task> tasks = taskRepository.getTasksRelatedToUser(username);
        User currentUser = userService.getUserByUsername(username);
        tasks.forEach(task -> {
            task.getUsers().remove(currentUser);
            taskRepository.save(task);
        });
    }

    private boolean isTaskInProject(Long userId, Long taskId) {
        return taskRepository.isUserAssignedAlreadyToTask(userId, taskId);
    }

    private void validateTaskProgress(int newProjectProgress, int oldProjectProgress) {
        if (newProjectProgress > 100 || newProjectProgress < 0) {
            throw new ValidationException("Invalid value. The progress must be between 0 and 100");
        }
        if (newProjectProgress < oldProjectProgress) {
            throw new ValidationException("Invalid value. The new progress must be greater than old.");
        }
    }

    private void validateSpentHours(int newSpentHours, int oldSpentHours) {
        if (newSpentHours < oldSpentHours) {
            throw new ValidationException("Invalid value. New spent hours must be greater than already spent.");
        }
    }
}
