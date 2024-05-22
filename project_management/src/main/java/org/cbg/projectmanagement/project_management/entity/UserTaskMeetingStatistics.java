package org.cbg.projectmanagement.project_management.entity;

import jakarta.faces.annotation.View;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Synchronize;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "project_user_task_meeting")
@Immutable
@View
@Synchronize({"userr", "project", "meeting", "task"})
public class UserTaskMeetingStatistics implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "key")
    private String projectKey;

    @Column(name = "title")
    private String projectTitle;

    @Column(name = "task_count")
    private Long remainingTasks;

    @Column(name = "meeting_date")
    private LocalDateTime meetingDate;

    @Column(name = "meeting_title")
    private String meetingTitle;
}
