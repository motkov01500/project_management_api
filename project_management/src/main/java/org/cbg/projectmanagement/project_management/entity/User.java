package org.cbg.projectmanagement.project_management.entity;

import jakarta.persistence.*;

import lombok.*;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Set;

@Entity
@Table(name = "userr" ,schema = "public")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userr_id_generator")
    @SequenceGenerator(name = "userr_id_generator", sequenceName = "userr_id_seq", schema = "public", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(targetEntity = Role.class)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany(mappedBy = "users")
    private Set<Project> projects;

    @ManyToMany(mappedBy = "users")
    private Set<Meeting> meetings;

    public User(String username, String password, String fullName, Role role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }
}
