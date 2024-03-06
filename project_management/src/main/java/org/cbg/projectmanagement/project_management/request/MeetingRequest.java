package org.cbg.projectmanagement.project_management.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MeetingRequest {

    private LocalDateTime date;
    private Long projectId;
}
