package bsuir.khanenko.modulepublisher.repository;

import bsuir.khanenko.modulepublisher.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User create(User user);
    User update(User updatedUser);
    void deleteById(Long id);
    List<User> findAll();
    Optional<User> findById(Long id);
}
