package org.cbg.projectmanagement.project_management.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cbg.projectmanagement.project_management.enumeration.TaskStatusEnum;

import jakarta.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
    @SequenceGenerator(name = "task_generator", sequenceName = "task_id_seq", schema = "public", allocationSize = 1)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(name = "progress")
    private int progress;

    @Column(name = "status")
    private String status;

    @Column(name = "initial_estimation")
    private int initialEstimation;

    @Column(name = "hours_spent")
    private int hoursSpent;

    @ManyToOne(targetEntity = Project.class)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(targetEntity = Meeting.class)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    public Task(int progress, String status, int initialEstimation,
                int hoursSpent, Project project, Meeting meeting) {
        this.progress = progress;
        this.status = status;
        this.initialEstimation = initialEstimation;
        this.hoursSpent = hoursSpent;
        this.project = project;
        this.meeting = meeting;
    }
}
