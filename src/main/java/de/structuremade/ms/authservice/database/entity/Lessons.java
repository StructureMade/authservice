package de.structuremade.ms.authservice.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "lessons", schema = "services", indexes = {
        @Index(name = "id_lessonrolesid", columnList = "id", unique = true)})
@Getter
@Setter
public class Lessons {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;
    @Column
    private String name;

    @Column(length = 3)
    private int state;

    @Column
    private String startOfLesson;

    @Column
    private String endOfLesson;

    @Column
    private String day;

    @OneToOne
    @JoinColumn(name = "userid", foreignKey = @ForeignKey(name = "fk_userid"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "schoolid", foreignKey = @ForeignKey(name = "fk_schoolid"))
    private School school;
}
