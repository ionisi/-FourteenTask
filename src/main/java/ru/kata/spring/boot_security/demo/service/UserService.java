package ru.kata.spring.boot_security.demo.service;
import ru.kata.spring.boot_security.demo.dto.UserCreateDTO;
import ru.kata.spring.boot_security.demo.dto.UserUpdateDTO;
import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();
    Optional<User> getByEmail(String email);
    User getById(int id);
    void save(UserCreateDTO user);
    void update(int id, UserUpdateDTO user);
    void delete(int id);
    Optional<User> findByUsername(String username);
}
