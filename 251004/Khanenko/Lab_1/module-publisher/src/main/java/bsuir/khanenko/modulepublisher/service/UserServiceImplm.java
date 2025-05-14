package bsuir.khanenko.modulepublisher.service;

import bsuir.khanenko.modulepublisher.dto.user.UserRequestTo;
import bsuir.khanenko.modulepublisher.dto.user.UserResponseTo;
import bsuir.khanenko.modulepublisher.dto.user.UserUpdate;
import bsuir.khanenko.modulepublisher.entity.User;
import bsuir.khanenko.modulepublisher.mapping.UserMapper;
import bsuir.khanenko.modulepublisher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplm implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Autowired
    public UserServiceImplm(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseTo create(UserRequestTo userRequestTo) {
        return userMapper.toUserResponseTo(userRepository.create(userMapper.toUser(userRequestTo)));
    }

    @Override
    public UserResponseTo update(UserUpdate updatedUser) {
        User user = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (updatedUser.getId() != null) {
            user.setId(updatedUser.getId());
        }
        if (updatedUser.getLogin() != null) {
            user.setLogin(updatedUser.getLogin());
        }
        if (updatedUser.getPassword() != null) {
            user.setPassword(updatedUser.getPassword());
        }
        if (updatedUser.getFirstname() != null) {
            user.setFirstname(updatedUser.getFirstname());
        }
        if (updatedUser.getLastname() != null) {
            user.setLastname(updatedUser.getLastname());
        }

        return userMapper.toUserResponseTo(userRepository.update(user));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponseTo> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponseTo)
                .toList();
    }

    @Override
    public Optional<UserResponseTo> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserResponseTo);
    }
}
