package org.cbg.projectmanagement.project_management.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "meeting", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meeting_generator")
    @SequenceGenerator(name = "meeting_generator", sequenceName = "meeting_id_seq", schema = "public", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "status", columnDefinition = "meeting_status")
    private String status;

    @ManyToOne(targetEntity = Project.class)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "user_meeting",
            joinColumns = {@JoinColumn(name = "meeting_id")},
            inverseJoinColumns = {@JoinColumn(name = "userr_id")})
    private Set<User> users;

    public Meeting(LocalDateTime date, String status, Project project) {
        this.date = date;
        this.status = status;
        this.project = project;
    }
}
