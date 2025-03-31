package ru.kata.spring.boot_security.demo.dao;
import ru.kata.spring.boot_security.demo.models.Role;
import java.util.List;
import java.util.Optional;

public interface RoleDAO {
    List<Role> getAll();
    Optional<Role> getRoleByName(String name);
    Role getById(int id);
    void save(Role role);
    void update(int id, Role role);
    void delete(int id);
}
