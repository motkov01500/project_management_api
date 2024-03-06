package org.cbg.projectmanagement.project_management.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MeetingDateUpdateRequest {

    private LocalDateTime date;
}
