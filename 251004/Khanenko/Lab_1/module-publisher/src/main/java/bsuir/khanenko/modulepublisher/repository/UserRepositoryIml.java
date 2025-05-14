package bsuir.khanenko.modulepublisher.repository;

import bsuir.khanenko.modulepublisher.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryIml implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public User create(User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User updatedUser) {

        if (!users.containsKey(updatedUser.getId())) {
            throw new IllegalArgumentException("User with ID " + updatedUser.getId() + " not found");
        }
        users.put(updatedUser.getId(), updatedUser);
        return updatedUser;
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }
}
