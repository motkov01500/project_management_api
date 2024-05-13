package org.cbg.projectmanagement.project_management.dto.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class UserUpdateImageDTO {

    private String imageUrl;
    private int size;
    private String fileType;
}
