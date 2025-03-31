package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserCreateDTO;
import ru.kata.spring.boot_security.demo.dto.UserResponseDTO;
import ru.kata.spring.boot_security.demo.dto.UserUpdateDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserMapper;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import java.util.*;

@Controller
@RequestMapping("/users")
public class AdminController {


    private final UserService userService;
    private final UserValidator userValidator;
    private final RoleService roleService;
    private final UserMapper modelMapper;

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator, RoleService roleService, UserMapper modelMapper) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String index(Model model, @ModelAttribute("user") User user) {
        model.addAttribute("users", userService.getAll());
        model.addAttribute("roles", roleService.getAll());
        return "admin_panel";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> create(@ModelAttribute("user") @Valid UserCreateDTO userCreateDTO,
                                    BindingResult bindingResult) {
        userValidator.validate(userCreateDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
        }
        userService.save(userCreateDTO);
        return ResponseEntity.ok().body(Map.of("success", true, "successMessage", "User created successfully!"));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody UserUpdateDTO dto) {
        if (userService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        userService.update(id, dto);
        return ResponseEntity.ok().body(Map.of("success", true, "successMessage", "User updated!"));
    }

    @GetMapping("/api/users")
    @ResponseBody
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> users = userService.getAll().stream()
                .map(modelMapper::userToUserResponseDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/roles")
    @ResponseBody
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleService.getAll());
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable int id) {
        userService.delete(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User deleted successfully!"
        ));
    }
}
