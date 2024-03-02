package org.cbg.projectmanagement.project_management.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.cbg.projectmanagement.project_management.enumeration.TaskStatusEnum;

import java.io.Serializable;


@Entity
@Table(name = "task")
@Data
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(name = "progress")
    private int progress;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatusEnum status;

    @Column(name = "initial_estimation")
    private int initialEstimation;

    @Column(name = "hours_spent")
    private int hoursSpent;

    @ManyToOne(targetEntity = Project.class)
    @JoinColumn(name = "project_id")
    private Project project;
}
