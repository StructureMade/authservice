package de.structuremade.ms.authservice.database.repo;

import de.structuremade.ms.authservice.database.entity.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionsRepository extends JpaRepository<Permissions, String> {

}
