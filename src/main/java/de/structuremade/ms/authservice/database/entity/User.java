package de.structuremade.ms.authservice.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", schema = "services", indexes = {
        @Index(name = "id_userid", columnList = "id", unique = true),
        @Index(name = "id_token", columnList = "token", unique = true),
        @Index(name = "id_email", columnList = "email", unique = true)})
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String name;

    @Column
    private String password;

    @Column(nullable = false)
    private Date creationDate;

    @Column
    private String token;

    @Column(nullable = false)
    private boolean verified;

    @Column
    private String lastSchool;

    @ManyToMany(targetEntity = School.class, fetch = FetchType.LAZY)
    @JoinTable(name = "userschools", schema = "services", joinColumns = @JoinColumn(name = "userid", foreignKey = @ForeignKey(name = "fk_userid"))
            , inverseJoinColumns = @JoinColumn(name = "schoolid", foreignKey = @ForeignKey(name = "fk_schoolid")))
    private List<School> schools = new ArrayList<>();

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "userroles", schema = "services", joinColumns = @JoinColumn(name = "userid", foreignKey = @ForeignKey(name = "fk_userid"))
            , inverseJoinColumns = @JoinColumn(name = "roleid", foreignKey = @ForeignKey(name = "fk_roleid")))
    private List<Role> roles = new ArrayList<>();

   @ManyToMany(targetEntity = Lessons.class)
    @JoinTable(name = "userlessons", schema = "services", joinColumns = @JoinColumn(name = "userid", foreignKey = @ForeignKey(name = "fk_userid"))
            , inverseJoinColumns = @JoinColumn(name = "lessonid", foreignKey = @ForeignKey(name = "fk_lessonid")))
    private List<Lessons> lessonRoles = new ArrayList<>();
}
