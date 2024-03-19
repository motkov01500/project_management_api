package org.cbg.projectmanagement.project_management.mapper;

import org.cbg.projectmanagement.project_management.dto.task.TaskResponseDTO;
import org.cbg.projectmanagement.project_management.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jsr330")
public interface TaskMapper {

    TaskResponseDTO mapTaskToTaskDTO(Task task);
}
