package de.structuremade.ms.authservice.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "services", indexes = {
        @Index(name = "id_permsid", columnList = "id", unique = true)
})
@Getter
@Setter
public class Permissions {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column
    private String name;

    @Column
    private boolean topicperm;

    @Column
    private boolean masterPerms;


    @OneToMany(targetEntity = Userpermissions.class)
    @JoinColumn(name = "permission")
    private List<Userpermissions> permissions;

}
