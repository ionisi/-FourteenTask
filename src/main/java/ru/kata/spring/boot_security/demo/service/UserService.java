package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();
    Optional<User> getByEmail(String email);
    User getById(int id);
    void save(User user);
    void update(int id, User user);
    void delete(int id);
    Optional<User> findByUsername(String username);
}
