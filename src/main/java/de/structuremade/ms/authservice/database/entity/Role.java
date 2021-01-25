package de.structuremade.ms.authservice.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles", schema = "services", indexes = {
        @Index(name = "id_rolesid", columnList = "id", unique = true)})
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "school")
    private School school;

    @ManyToMany(targetEntity = Permissions.class, fetch = FetchType.LAZY)
    @JoinTable(name = "rolepermissions", schema = "services",
            joinColumns = @JoinColumn(name = "role", foreignKey = @ForeignKey(name = "fk_role"))
            , inverseJoinColumns = @JoinColumn(name = "permission", foreignKey = @ForeignKey(name = "fk_permission")))
    private List<Permissions> permissions = new ArrayList<>();
}

