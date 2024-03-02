package org.cbg.projectmanagement.project_management.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name="project")
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(name = "key", unique = true, nullable = false)
    private String key;

    @Column(name = "title")
    private String title;

    @ManyToMany
    @JoinTable(name = "user_project",
                joinColumns = {@JoinColumn(name = "userr_id")},
                inverseJoinColumns = {@JoinColumn(name = "project_id")})
    private Set<User> users;
}
