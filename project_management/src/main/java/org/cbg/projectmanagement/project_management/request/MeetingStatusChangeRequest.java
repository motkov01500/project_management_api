package org.cbg.projectmanagement.project_management.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MeetingStatusChangeRequest {

    private String status;
}