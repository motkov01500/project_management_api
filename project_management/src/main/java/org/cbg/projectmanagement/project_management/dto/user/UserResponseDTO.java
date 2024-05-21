package org.cbg.projectmanagement.project_management.dto.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.cbg.projectmanagement.project_management.dto.meeting.RecentMeetingDTO;
import org.cbg.projectmanagement.project_management.dto.project.ProjectResponseDTO;
import org.cbg.projectmanagement.project_management.dto.role.RoleResponseDTO;

import java.sql.Blob;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
public class UserResponseDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private Boolean isDeleted;
    private RoleResponseDTO role;
}
