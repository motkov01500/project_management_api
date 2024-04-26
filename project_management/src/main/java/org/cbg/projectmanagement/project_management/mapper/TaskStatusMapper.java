package org.cbg.projectmanagement.project_management.mapper;

import org.cbg.projectmanagement.project_management.dto.task_status.TaskStatusResponseDTO;
import org.cbg.projectmanagement.project_management.entity.TaskStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jsr330")
public interface TaskStatusMapper {

    TaskStatusResponseDTO mapTaskResponseToTaskResponse(TaskStatus taskStatus);
}
