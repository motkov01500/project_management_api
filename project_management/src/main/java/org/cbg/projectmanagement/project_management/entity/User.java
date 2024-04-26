package org.cbg.projectmanagement.project_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
//
//    public void setPassword(String password) {
//        Matcher matcher = Pattern.compile("(?=.*[a-z])(?=.*[A-Z]).{8,}")
//                .matcher(password);
//        if(!matcher.find()) {
//            throw new ValidationException("Wrong password", Response
//                    .status(Response.Status.BAD_REQUEST)
//                    .entity(Json.createObjectBuilder()
//                            .add("message", "Password must contains minimum one uppercase, one lowercase and one digit and must be at least 8 characters")
//                            .build())
//                    .build());
//        }
//        this.password = password;
//    }
}
