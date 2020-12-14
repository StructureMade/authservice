package de.structuremade.ms.authservice.database.repo;


import de.structuremade.ms.authservice.database.entity.Role;
import de.structuremade.ms.authservice.database.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RoleRepository extends JpaRepository<Role, String> {

    List<Role> findAllBySchool(School school);
}
