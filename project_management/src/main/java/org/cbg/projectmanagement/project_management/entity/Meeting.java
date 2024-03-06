package org.cbg.projectmanagement.project_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cbg.projectmanagement.project_management.enumeration.MeetingStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "meeting", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Meeting implements Serializable {

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

    public Meeting(LocalDateTime date, String status, Project project) {
        this.date = date;
        this.status = status;
        this.project = project;
    }
}
