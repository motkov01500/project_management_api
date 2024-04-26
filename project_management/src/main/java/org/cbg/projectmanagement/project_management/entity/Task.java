package org.cbg.projectmanagement.project_management.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;


@Entity
@Table(name = "task", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Task  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
    @SequenceGenerator(name = "task_generator", sequenceName = "task_id_seq", schema = "public", allocationSize = 1)
    @Column(name="id", nullable = false, unique = true)
    private Long id;

    @Column(name = "progress")
    private int progress;

    @Column(name = "initial_estimation")
    private int initialEstimation;

    @Column(name = "hours_spent")
    private int hoursSpent;

    @ManyToOne(targetEntity = Project.class)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(targetEntity = TaskStatus.class)
    @JoinColumn(name="task_status_id")
    private TaskStatus taskStatus;

    public Task(int progress, int initialEstimation,
                int hoursSpent, Project project, TaskStatus taskStatus) {
        this.progress = progress;
        this.initialEstimation = initialEstimation;
        this.hoursSpent = hoursSpent;
        this.project = project;
        this.taskStatus = taskStatus;
    }
}
