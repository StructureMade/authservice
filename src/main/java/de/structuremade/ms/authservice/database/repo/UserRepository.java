package de.structuremade.ms.authservice.database.repo;

import de.structuremade.ms.authservice.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    User findSchoolByEmail(String email);

    User findAllById(String id);
}
