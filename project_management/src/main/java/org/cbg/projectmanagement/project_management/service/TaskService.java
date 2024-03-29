package org.cbg.projectmanagement.project_management.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.task.TaskCreateDTO;
import org.cbg.projectmanagement.project_management.dto.task.TaskUpdateProgressDTO;
import org.cbg.projectmanagement.project_management.entity.Meeting;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.entity.Task;
import org.cbg.projectmanagement.project_management.repository.TaskRepository;

import java.util.List;

@Named("TaskService")
public class TaskService {

    @Inject
    TaskRepository taskRepository;

    @Inject
    ProjectService projectService;

    @Inject
    MeetingService meetingService;

    public List<Task> findAll() {
        return taskRepository
                .findAll();
    }

    public Task findById(Long id) {
        return taskRepository
                .findById(id);
    }

    @Transactional
    public Task create(TaskCreateDTO taskCreateDTO) {
        Project project = projectService.findById(taskCreateDTO.getProjectId());
        Meeting meeting = meetingService.findById(taskCreateDTO.getMeetingId());
        Task newTask = new Task(taskCreateDTO.getProgress(), taskCreateDTO.getStatus(),
                taskCreateDTO.getInitialEstimation(), 0, project, meeting);
        taskRepository.create(newTask);
        return newTask;
    }

    public Task updateProgressForUser(Long id, TaskUpdateProgressDTO taskUpdateProgressDTO) {
        Task currentTask = null;
        if (projectService.isUserInProject(taskUpdateProgressDTO.getProjectKey())) {
            currentTask = updateProgress(id, taskUpdateProgressDTO);
        }
        return currentTask == null ? findById(id) : currentTask;
    }

    public Task updateProgress(Long id, TaskUpdateProgressDTO taskUpdateProgressDTO) {
        Task currentTask = findById(id);
        currentTask.setProgress(taskUpdateProgressDTO.getProgress());
        taskRepository.update(currentTask);
        return currentTask;
    }

    public void delete(Long id) {
        taskRepository.delete(id);
    }
}
