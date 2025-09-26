package org.gaboegui.backend.usersapp.services;

import org.gaboegui.backend.usersapp.models.entities.Role;
import org.gaboegui.backend.usersapp.models.entities.User;
import org.gaboegui.backend.usersapp.models.entities.dto.UserDto;
import org.gaboegui.backend.usersapp.models.entities.dto.UserDtoMapper;
import org.gaboegui.backend.usersapp.repository.RoleRepository;
import org.gaboegui.backend.usersapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    // configured in SpringSecurityConfig.java
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User> users = (List<User>) repository.findAll();
        return users
                .stream()
                .map(us -> UserDtoMapper.builder().setUser(us).build())
                .collect(Collectors.toList());
    }

    /**
     * New method for obtain users with pagination
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> usersPage = repository.findAll(pageable);
        return usersPage.map(us -> UserDtoMapper.builder().setUser(us).build());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Long id) {
        Optional<User> opt = repository.findById(id);
        return opt.map(user -> UserDtoMapper.builder().setUser(user).build());
    }

    @Override
    @Transactional
    public UserDto save(User user) {

        // coded by BCrypt hash
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRolesFromUser(user));

        return UserDtoMapper.builder().setUser(repository.save(user)).build();
    }

    @Transactional
    public UserDto update(UserDto user, Long id) {

        Optional<User> oUser = repository.findById(id);

        if (oUser.isPresent()){

            User userToUpdate = oUser.get();
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setAdmin(user.isAdmin());

            // get the roles directly from db
            userToUpdate.setRoles(getRolesFromUser(oUser.get()));

            return UserDtoMapper.builder().setUser(repository.save(userToUpdate)).build();
        } else {
            return null;
        }
    }

    private List<Role> getRolesFromUser(User user){

        List<Role> roles = new ArrayList<>();
        // this is the default added role
        Optional<Role> op = roleRepository.findByName("ROLE_USER");
        roles.add(op.orElseThrow());

        if (user.isAdmin()){
            Optional<Role> oAdmin = roleRepository.findByName("ROLE_ADMIN");
            roles.add(oAdmin.orElseThrow());
        }
        return roles;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
