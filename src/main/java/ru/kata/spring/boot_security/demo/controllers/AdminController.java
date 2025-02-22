package ru.kata.spring.boot_security.demo.controllers;


import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("/admin")
public class AdminController {


    private final UserService userService;
    private final UserValidator userValidator;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator, RoleService roleService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.roleService = roleService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", userService.getAll());
        return "index";
    }

    @GetMapping("/show")
    public String show(@RequestParam("id") int id, Model model) {
        model.addAttribute("user", userService.getById(id));
        return "show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("user") User user, Model model) {
        List<Role> roles = roleService.getAll();
        model.addAttribute("roles", roles);
        return "new";
    }

    @PostMapping("/users")
    public String create(@ModelAttribute("user") @Valid User user,
                         @RequestParam("roles") List<Integer> roleids,
                         BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "new";
        }

        List<Role> roles = roleids.stream().map(roleService::getById).filter(Objects::nonNull).toList();
        user.setRoles(new ArrayList<>(roles));
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.getById(id));
        List<Role> roles = roleService.getAll();
        model.addAttribute("roles", roles);
        return "edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult, @RequestParam("id") int id,
                         @RequestParam("roles") List<Integer> roleids) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "edit";
        }
        List<Role> roles = roleids.stream().map(roleService::getById).filter(Objects::nonNull).toList();


        user.setRoles(new ArrayList<>(roles));
        System.out.println(user.getRoles().getClass());
        userService.update(id, user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
