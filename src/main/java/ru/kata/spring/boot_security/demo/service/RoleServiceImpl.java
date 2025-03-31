package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDAO;
import ru.kata.spring.boot_security.demo.models.Role;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleDAO roleDAO;

    @Autowired
    public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAll() {
        return roleDAO.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> getRoleByName(String name) {
        return roleDAO.getRoleByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Role getById(int id) {
        return roleDAO.getById(id);
    }

    @Override
    @Transactional
    public void save(Role role) {
        roleDAO.save(role);
    }

    @Override
    @Transactional
    public void update(int id, Role role) {
        roleDAO.update(id, role);
    }

    @Override
    @Transactional
    public void delete(int id) {
        roleDAO.delete(id);
    }
}
