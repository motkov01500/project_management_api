package org.cbg.projectmanagement.project_management.enumeration;

import lombok.Getter;

@Getter
public enum MeetingStatus {
    UPCOMING("UPCOMING"), START("START"), END("END");

    private String name;

    MeetingStatus(String name) {
        this.name = name;
    }
}
