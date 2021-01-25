package de.structuremade.ms.authservice.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
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

    @Column
    private String abbreviation;

    @Column(nullable = false)
    private Date creationDate;

    @Column
    private String token;

    @Column(nullable = false)
    private boolean verified;

    @Column
    private String lastSchool;

    @ManyToMany(targetEntity = School.class)
    @JoinTable(name = "userschools", schema = "services", joinColumns = @JoinColumn(name = "userid", foreignKey = @ForeignKey(name = "fk_userid"))
            , inverseJoinColumns = @JoinColumn(name = "school", foreignKey = @ForeignKey(name = "fk_school")))
    private List<School> schools = new ArrayList<>();

    @ManyToMany(targetEntity = Role.class)
    @JoinTable(name = "userroles", schema = "services", joinColumns = @JoinColumn(name = "userid", foreignKey = @ForeignKey(name = "fk_userid"))
            , inverseJoinColumns = @JoinColumn(name = "role", foreignKey = @ForeignKey(name = "fk_role")))
    private List<Role> roles = new ArrayList<>();

    @ManyToMany(targetEntity = User.class)
    @JoinTable(name = "userparents", schema = "services", joinColumns = @JoinColumn(name = "parent", foreignKey = @ForeignKey(name = "fk_parent"))
            , inverseJoinColumns = @JoinColumn(name = "student", foreignKey = @ForeignKey(name = "fk_student")))
    private List<User> parents;

    @ManyToMany(targetEntity = User.class)
    @JoinTable(name = "userparents", schema = "services", joinColumns = @JoinColumn(name = "parent", foreignKey = @ForeignKey(name = "fk_parent"))
            , inverseJoinColumns = @JoinColumn(name = "student", foreignKey = @ForeignKey(name = "fk_student")))
    private List<User> childrens;

    @OneToMany(targetEntity = Userpermissions.class)
    @JoinColumn(name = "userid")
    private List<Userpermissions> permissions;

    @ManyToOne(targetEntity = Class.class)
    @JoinColumn(name = "class", foreignKey = @ForeignKey(name = "fk_class"))
    private Class userClass;
}
