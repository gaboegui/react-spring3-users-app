package org.gaboegui.backend.usersapp.repository;


import org.gaboegui.backend.usersapp.models.entities.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String rolename);
}
