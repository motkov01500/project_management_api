package org.cbg.projectmanagement.project_management.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskRequest {

    private int progress;
    private String status;
    private int initialEstimation;
    private Long projectId;
    private Long meetingId;
}
