package org.cbg.projectmanagement.project_management.entity;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "project", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
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

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_project",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "userr_id")})
    private List<User> users;

    @OneToMany(mappedBy = "project", orphanRemoval = true, cascade = CascadeType.REMOVE, targetEntity = Meeting.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Meeting> meetingSet;

    @OneToMany(mappedBy = "project", orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = Task.class)
    private List<Task> taskSet;

    public Project(String key, String title, Boolean isDeleted) {
        this.key = key;
        this.title = title;
        this.isDeleted = isDeleted;
    }
}
