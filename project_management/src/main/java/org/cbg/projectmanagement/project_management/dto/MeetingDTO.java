package org.cbg.projectmanagement.project_management.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cbg.projectmanagement.project_management.enumeration.MeetingStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MeetingDTO {

    private LocalDateTime date;
    private MeetingStatus status;
}
