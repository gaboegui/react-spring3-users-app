package org.gaboegui.backend.usersapp.controller;

import jakarta.validation.Valid;
import org.gaboegui.backend.usersapp.models.entities.User;
import org.gaboegui.backend.usersapp.models.entities.dto.UserDto;
import org.gaboegui.backend.usersapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST controller to manage users
 * CrossOrigin will be limited in SpringSecurityConfig.java
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(originPatterns = "*") // all domains are allowed to call the WS
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public List<UserDto> listUsers() {
        return service.findAll();
    }

    @GetMapping("/page/{page}")
    public Page<UserDto> listUserByPage(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page,5 );
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<UserDto> userOpt = service.findById(id);

        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult validation) {

        if (validation.hasErrors()) {
            return processResult(validation);
        }
        UserDto saved = null;

        try {
            saved = service.save(user);

        } catch (Exception e) {
            Map<String, String> errors = new HashMap<>();
            String[] segments =  e.getMessage().split("\\]");
            errors.put("createUser Error: ", segments[0]);
            return ResponseEntity.badRequest().body(errors);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDto user, BindingResult validation, @PathVariable Long id) {

        if (validation.hasErrors()) {
            return processResult(validation);
        }

        UserDto updated = service.update(user, id);
         if (updated != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<UserDto> fromDb = service.findById(id);
        if (fromDb.isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

     private ResponseEntity<?> processResult(BindingResult result) {

        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getField() + ": " + err.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

}