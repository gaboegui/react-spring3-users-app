package org.gaboegui.backend.usersapp.services;

import org.gaboegui.backend.usersapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class to consult DB to get user credentials
 */
@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //should match against DB
        Optional<org.gaboegui.backend.usersapp.models.entities.User> optional =
                repository.findByUsername(username);

        if (!optional.isPresent()) {
            throw new UsernameNotFoundException("Username or Password incorrect");
        }

        org.gaboegui.backend.usersapp.models.entities.User userDb = optional.orElseThrow();

        // get the user roles from DB
        List<GrantedAuthority> authorities = userDb.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        // password is encrypted by BCrypt and comes from DB
        return new User(userDb.getUsername(),
                userDb.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}
