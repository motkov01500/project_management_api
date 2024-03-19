package org.cbg.projectmanagement.project_management.dto.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {

    private int progress;
    private String status;
    private int initialEstimation;
    private Long projectId;
    private Long meetingId;
}
