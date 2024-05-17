package org.cbg.projectmanagement.project_management.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "meeting", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Meeting implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meeting_generator")
    @SequenceGenerator(name = "meeting_generator", sequenceName = "meeting_id_seq", schema = "public", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "title")
    private String title;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "duration")
    private int duration;

    @ManyToOne(targetEntity = Project.class)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = User.class)
    @JoinTable(name = "user_meeting",
            joinColumns = {@JoinColumn(name = "meeting_id")},
            inverseJoinColumns = {@JoinColumn(name = "userr_id")})
    private List<User> users;

    public Meeting(LocalDateTime date, String title, Project project, Boolean isDeleted) {
        this.date = date;
        this.title = title;
        this.project = project;
        this.isDeleted = isDeleted;
    }
}
