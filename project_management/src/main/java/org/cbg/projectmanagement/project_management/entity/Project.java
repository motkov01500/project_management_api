package org.cbg.projectmanagement.project_management.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "project", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_id_generator")
    @SequenceGenerator(name = "project_id_generator", sequenceName = "project_id_seq", schema = "public", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
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

    public Project(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
