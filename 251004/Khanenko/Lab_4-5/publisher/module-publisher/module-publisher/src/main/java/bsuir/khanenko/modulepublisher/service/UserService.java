package bsuir.khanenko.modulepublisher.service;

import bsuir.khanenko.modulepublisher.dto.user.UserRequestTo;
import bsuir.khanenko.modulepublisher.dto.user.UserResponseTo;
import bsuir.khanenko.modulepublisher.dto.user.UserUpdate;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseTo create(UserRequestTo user);
    UserResponseTo update(UserUpdate updatedUser);
    void deleteById(Long id);
    List<UserResponseTo> findAll();
    Optional<UserResponseTo> findById(Long id);
}

