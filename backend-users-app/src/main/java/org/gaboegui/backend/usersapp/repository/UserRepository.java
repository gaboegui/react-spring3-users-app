package org.gaboegui.backend.usersapp.repository;


import org.gaboegui.backend.usersapp.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Page<User> findAll(Pageable pageable);
}
