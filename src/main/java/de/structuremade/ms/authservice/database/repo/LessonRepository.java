package de.structuremade.ms.authservice.database.repo;

import de.structuremade.ms.authservice.database.entity.Lessons;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lessons, String> {
}
