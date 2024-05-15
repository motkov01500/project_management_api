package org.cbg.projectmanagement.project_management.dto.meeting;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class MeetingUnAssignUserDTO {

    private Long meetingId;
    private Long userId;
}
