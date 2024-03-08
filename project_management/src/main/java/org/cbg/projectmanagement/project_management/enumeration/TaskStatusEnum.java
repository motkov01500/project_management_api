package org.cbg.projectmanagement.project_management.enumeration;

import lombok.Getter;

@Getter
public enum TaskStatusEnum {
    D("Done"),I("In progress"),O("Open"),R("Reopened");

    private String name;

    TaskStatusEnum(String name) {
        this.name = name;
    }
}