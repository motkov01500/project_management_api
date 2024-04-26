package org.cbg.projectmanagement.project_management.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.task_status.TaskStatusCreateDTO;
import org.cbg.projectmanagement.project_management.entity.TaskStatus;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.repository.TaskStatusRepository;

import java.util.List;

@Stateless
public class TaskStatusService {

    @Inject
    private TaskStatusRepository taskStatusRepository;

    public List<TaskStatus> findAll() {
        return taskStatusRepository
                .findAll();
    }

    public TaskStatus findByName(String name) throws NotFoundResourceException{
        return taskStatusRepository
                .findByName(name)
                .orElseThrow(()-> new NotFoundResourceException("Status not found"));
    }

    public TaskStatus create(TaskStatusCreateDTO taskStatusCreateDTO) {
        TaskStatus newTaskStatus = new TaskStatus(taskStatusCreateDTO.getName().toUpperCase());
        taskStatusRepository.create(newTaskStatus);
        return newTaskStatus;
    }
}
