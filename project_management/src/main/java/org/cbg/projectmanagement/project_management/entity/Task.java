package org.cbg.projectmanagement.project_management.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import org.cbg.projectmanagement.project_management.enums.TaskStatus;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "task", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
    @SequenceGenerator(name = "task_generator", sequenceName = "task_id_seq", schema = "public", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "progress")
    private int progress;

    @Column(name = "is_user_available_to_add")
    private Boolean isUsersAvailable;

    @Column(name = "task_status")
    private String taskStatus;

    @Column(name = "initial_estimation")
    private int initialEstimation;

    @Column(name = "hours_spent")
    private int hoursSpent;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToOne(targetEntity = Project.class)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = User.class)
    @JoinTable(name = "user_task",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "userr_id")})
    private List<User> users;

    public Task(int progress, int initialEstimation, int hoursSpent, Project project, String taskStatus, Boolean isDeleted) {
        this.progress = progress;
        this.taskStatus = taskStatus;
        this.initialEstimation = initialEstimation;
        this.hoursSpent = hoursSpent;
        this.project = project;
        this.isDeleted = isDeleted;
    }
}
