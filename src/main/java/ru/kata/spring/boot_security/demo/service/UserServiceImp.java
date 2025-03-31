package ru.kata.spring.boot_security.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.dto.UserCreateDTO;
import ru.kata.spring.boot_security.demo.dto.UserUpdateDTO;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.util.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    private final UserDAO userDAO;
    private final UserMapper userMapper;
    private final RoleService roleService;

    @Autowired
    public UserServiceImp(UserDAO userDAO, UserMapper userMapper, RoleService roleService) {
        this.userDAO = userDAO;
        this.userMapper = userMapper;
        this.roleService = roleService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userDAO.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getByEmail(String email) {
        return userDAO.getByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(int id) {
        return userDAO.getById(id);
    }

    @Override
    @Transactional
    public void save(UserCreateDTO userCreateDTO) {
        User user = userMapper.userCreateDtoToUser(userCreateDTO);
        user.setRoles(new ArrayList<>(userCreateDTO.getRoles().stream().map(roleService::getById).filter(Objects::nonNull).toList()));
        userDAO.save(user);
    }

    @Override
    @Transactional
    public void update(int id, UserUpdateDTO dto) {
        User user = userMapper.DtoToUser(dto);
        user.setRoles(new ArrayList<>(dto.getRoles().stream().map(roleService::getById).filter(Objects::nonNull).toList()));
        userDAO.update(id, user);
    }

    @Override
    @Transactional
    public void delete(int id) {
        userDAO.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
}