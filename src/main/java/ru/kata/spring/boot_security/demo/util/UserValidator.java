package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.dto.UserCreateDTO;
import ru.kata.spring.boot_security.demo.service.UserService;

@Component
public class UserValidator implements Validator {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserCreateDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserCreateDTO userCreateDTO = (UserCreateDTO) o;
        if (userService.getByEmail(userCreateDTO.getEmail()).isPresent()) {
            errors.rejectValue("email", "email.duplicate", "Email is already in use");
        }
    }
}
