package org.cbg.projectmanagement.project_management.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.cbg.projectmanagement.project_management.entity.Meeting;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.entity.Task;
import org.cbg.projectmanagement.project_management.repository.TaskRepository;
import org.cbg.projectmanagement.project_management.request.TaskRequest;
import org.cbg.projectmanagement.project_management.request.TaskUpdateRequest;

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
    public Task create(TaskRequest request) {
        Project project = projectService.findById(request.getProjectId());
        Meeting meeting = meetingService.findById(request.getMeetingId());
        Task newTask = new Task(request.getProgress(), request.getStatus(),
                request.getInitialEstimation(), 0, project, meeting);
        taskRepository.create(newTask);
        return newTask;
    }

    public Task updateProgress(Long id, TaskUpdateRequest request) {
        Task task = findById(id);
        Meeting currentMeeting = task.getMeeting();
        if (!currentMeeting.getStatus().equalsIgnoreCase("done")) {
            task.setProgress(request.getProgress());
        }
        taskRepository.update(task);
        return task;
    }

    public void delete(Long id) {
        taskRepository.delete(id);
    }
}
