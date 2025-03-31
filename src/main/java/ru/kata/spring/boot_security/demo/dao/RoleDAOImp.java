package ru.kata.spring.boot_security.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleDAOImp implements RoleDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAll() {
        return entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> getRoleByName(String name) {
        return entityManager.createQuery("SELECT r FROM Role r WHERE r.role = :name", Role.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Role getById(int id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    @Transactional
    public void save(Role role) {
        if (getRoleByName(role.getRole()).isEmpty()) {
            entityManager.merge(role);
        }
    }

    @Override
    @Transactional
    public void update(int id, Role role) {
        Role existedRole = entityManager.find(Role.class, id);
        if (existedRole != null) {
            existedRole.setRole(role.getRole());
            entityManager.merge(existedRole);
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        Role role = entityManager.find(Role.class, id);
        if (role != null) {
            entityManager.remove(role);
        }
    }
}
