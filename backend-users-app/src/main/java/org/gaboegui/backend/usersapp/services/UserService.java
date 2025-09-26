package org.gaboegui.backend.usersapp.services;

import org.gaboegui.backend.usersapp.models.entities.User;
import org.gaboegui.backend.usersapp.models.entities.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAll();

    // for pagination use
    Page<UserDto> findAll(Pageable pageable);

    Optional<UserDto> findById(Long id);

    UserDto save(User user);

    public UserDto update(UserDto user, Long id);

    void delete (Long id);

}
