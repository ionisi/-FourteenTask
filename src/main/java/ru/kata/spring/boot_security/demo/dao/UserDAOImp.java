package ru.kata.spring.boot_security.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserDAOImp implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDAOImp(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return entityManager.createQuery("select p from User p", User.class).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getByEmail(String email) {
        return entityManager.createQuery("SELECT p FROM User p WHERE p.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        List<Role> roles = user.getRole().stream()
                .map(role -> entityManager.find(Role.class, role.getRole()))
                .filter(Objects::nonNull)
                .toList();

        user.setRole(roles);

        for (Role role : roles) {
            role.getUsers().add(user);
            entityManager.merge(role);
        }

        entityManager.persist(user);
        entityManager.flush();
    }

    @Override
    @Transactional
    public void update(int id, User user) {
        User existingUser = entityManager.find(User.class, id);
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setAge(user.getAge());
            existingUser.setEmail(user.getEmail());
            if (!user.getPassword().equals(existingUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            List<Role> roles = user.getRole().stream()
                    .map(role -> entityManager.find(Role.class, role.getId()))
                    .filter(Objects::nonNull)
                    .toList();
            existingUser.setRole(roles);
        }
        entityManager.merge(existingUser);
    }

    @Override
    @Transactional
    public void delete(int id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

}
