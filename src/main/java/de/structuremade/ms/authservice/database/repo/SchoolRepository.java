package de.structuremade.ms.authservice.database.repo;

import de.structuremade.ms.authservice.database.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, String> {

    School findAllById(String lastSchool);
}
