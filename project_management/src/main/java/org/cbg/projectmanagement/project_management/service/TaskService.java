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

    @Inject
    UserService userService;

    @Context
    SecurityContext context;

    public List<Task> findAll() {
        return taskRepository
                .findAll();
    }

    public Task findById(Long id) {
        return taskRepository
                .findById(id);
    }

    public String userName() {
        return context.getUserPrincipal().getName();
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

    //TODO:Make for user and administrator
    public Task updateProgress(Long id, TaskUpdateProgressDTO taskUpdateProgressDTO) {
        String currentLoggedUserUsername = context.getUserPrincipal().getName();
        Task task = findById(id);
//        if(context.isUserInRole("user")) {
//            if (!projectService.isUserInProject(currentLoggedUserUsername) &&
//                    !task.getMeeting().getStatus().equalsIgnoreCase("done")) {
//                task.setProgress(taskUpdateProgressDTO.getProgress());
//            }
//        } else {
//            task.setProgress(taskUpdateProgressDTO.getProgress());
//        }
        taskRepository.update(task);
        return task;
    }

    public void delete(Long id) {
        taskRepository.delete(id);
    }
}
