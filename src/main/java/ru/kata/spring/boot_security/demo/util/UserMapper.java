package ru.kata.spring.boot_security.demo.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserCreateDTO;
import ru.kata.spring.boot_security.demo.dto.UserResponseDTO;
import ru.kata.spring.boot_security.demo.dto.UserUpdateDTO;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;

@Component
public class UserMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public User DtoToUser(UserUpdateDTO userUpdateDTO) {
        return modelMapper.map(userUpdateDTO, User.class);
    }

    public UserResponseDTO userToUserResponseDTO(User u) {
        return modelMapper.map(u, UserResponseDTO.class);
    }

    public User userCreateDtoToUser(UserCreateDTO userCreateDTO) {
        return modelMapper.map(userCreateDTO, User.class);
    }
}
