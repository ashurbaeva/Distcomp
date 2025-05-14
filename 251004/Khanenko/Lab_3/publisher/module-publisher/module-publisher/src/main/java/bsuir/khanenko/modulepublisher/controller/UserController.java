package bsuir.khanenko.modulepublisher.controller;

import bsuir.khanenko.modulepublisher.dto.user.UserRequestTo;
import bsuir.khanenko.modulepublisher.dto.user.UserResponseTo;
import bsuir.khanenko.modulepublisher.dto.user.UserUpdate;
import bsuir.khanenko.modulepublisher.exceptionHandler.UserNotFoundException;
import bsuir.khanenko.modulepublisher.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/v1.0/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseTo>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseTo> findById(@PathVariable @Valid @NotNull Long id) {
        Optional<UserResponseTo> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponseTo> create(@Valid @RequestBody UserRequestTo userRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(userRequestTo));

    }

    @PutMapping()
    public ResponseEntity<UserResponseTo> update(@Valid @RequestBody UserUpdate userUpdate) {
        return ResponseEntity.ok(userService.update(userUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Valid @NotNull Long id) {
        userService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}