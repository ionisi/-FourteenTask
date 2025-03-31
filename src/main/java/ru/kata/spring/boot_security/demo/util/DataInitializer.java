package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserCreateDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Collections;

@Component
public class DataInitializer implements ApplicationRunner {
    private final UserService userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public DataInitializer(final UserService userService, RoleServiceImpl roleServiceImpl) {
        this.userService = userService;
        this.roleService = roleServiceImpl;
    }

    @Override
    public void run(ApplicationArguments args) {

        Role adminRole = roleService.getRoleByName("ROLE_ADMIN").orElseGet(() -> {
            Role newAdminRole = new Role("ROLE_ADMIN");
            roleService.save(newAdminRole);
            return roleService.getRoleByName("ROLE_ADMIN").get();
        });

        Role userRole = roleService.getRoleByName("ROLE_USER").orElseGet(() -> {
            Role newuserRole = new Role("ROLE_USER");
            roleService.save(newuserRole);
            return roleService.getRoleByName("ROLE_USER").get();
        });


        if (userService.getAll().stream()
                .noneMatch(user -> user.getUsername().equals("admin admin"))) {
            UserCreateDTO admin = new UserCreateDTO();
            admin.setFirstname("admin");
            admin.setLastname("admin");
            admin.setPassword("adminpassword");
            admin.setAge(100);
            admin.setEmail("admin@email.com");
            admin.setRoles(Collections.singletonList(1));
            userService.save(admin);
        }
    }
}
