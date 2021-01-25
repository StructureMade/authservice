package de.structuremade.ms.authservice.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "userpermissions")
@Getter
@Setter
public class Userpermissions {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;

    @ManyToOne(targetEntity = Permissions.class)
    @JoinColumn(name = "permission", foreignKey = @ForeignKey(name = "fk_permission"))
    private Permissions permission;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "userid", foreignKey = @ForeignKey(name = "fk_userid"))
    private User user;

    @ManyToOne(targetEntity = School.class)
    @JoinColumn(name = "school", foreignKey = @ForeignKey(name = "fk_school"))
    private School school;

}
