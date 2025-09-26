package org.gaboegui.backend.usersapp.models.entities.dto;

import org.gaboegui.backend.usersapp.models.entities.User;

public class UserDtoMapper {

    private User user;
    private UserDtoMapper() {
    }

    public static UserDtoMapper builder(){
        return new UserDtoMapper();
    }
    public UserDtoMapper setUser(User user) {
        this.user = user;
        return this;
    }

    public UserDto build(){
        if (this.user == null){
            throw new RuntimeException("Must provide User entity");
        }

        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        return new UserDto(this.user.getId(), this.user.getUsername(), this.user.getEmail(), isAdmin);

    }
}
