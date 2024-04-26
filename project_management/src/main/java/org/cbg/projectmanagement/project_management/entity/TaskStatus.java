package org.cbg.projectmanagement.project_management.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="task_status", schema = "public")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_status_generator")
    @SequenceGenerator(name = "task_status_generator", sequenceName = "task_status_id_seq", schema = "public", allocationSize = 1)
    @Column(name="id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name")
    private String name;

    public TaskStatus(String name) {
        this.name = name;
    }
}
