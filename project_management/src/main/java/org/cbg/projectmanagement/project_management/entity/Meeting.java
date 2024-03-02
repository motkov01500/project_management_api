package org.cbg.projectmanagement.project_management.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.cbg.projectmanagement.project_management.enumeration.MeetingStatusEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "meeting")
@Data
public class Meeting implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MeetingStatusEnum status;

    @ManyToOne(targetEntity = Project.class)
    @JoinColumn(name = "project_id")
    private Project project;
}
