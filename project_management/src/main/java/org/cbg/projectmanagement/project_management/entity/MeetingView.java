package org.cbg.projectmanagement.project_management.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity(name = "meeting_view")
@Immutable
public class MeetingView {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "date")
    private LocalDateTime date;

    private boolean isUserSpaceAvailable;
}
