package ru.kata.spring.boot_security.demo.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserCreateDTO;
import ru.kata.spring.boot_security.demo.dto.UserResponseDTO;
import ru.kata.spring.boot_security.demo.dto.UserUpdateDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(ModelMapper modelMapper, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public void DtoToUser(UserUpdateDTO userUpdateDTO, User user) {
        String password = user.getPassword();
        modelMapper.map(userUpdateDTO, user);

        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        } else {
            user.setPassword(password);
        }
        user.setRoles(mapRoleIds(userUpdateDTO.getRoles()));
    }

    private List<Role> mapRoleIds(List<Integer> roleIds) {
        if (roleIds == null) {
            return Collections.emptyList();
        }
        return roleIds.stream().map(roleService::getById).filter(Objects::nonNull)
                .toList();
    }

    public UserResponseDTO userToUserResponseDTO(User u) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        modelMapper.map(u, userResponseDTO);
        return userResponseDTO;
    }



    public UserUpdateDTO userToDto (User u) {
        UserUpdateDTO dto = new UserUpdateDTO();
        modelMapper.map(u, dto);
        dto.setRoles(mapRoles(u.getRoles()));
        return dto;
    }

    private List<Integer> mapRoles(List<Role> roles) {
        if (roles == null) {
            return Collections.emptyList();
        }
        return roles.stream().map(Role::getId).distinct().toList();
    }

    public User userCreateDtoToUser(UserCreateDTO userCreateDTO) {
        User user = new User();
        modelMapper.map(userCreateDTO, user);
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        List<Role> roles = userCreateDTO.getRoles().stream().map(roleService::getById).filter(Objects::nonNull).toList();
        user.setRoles(new ArrayList<>(roles));
        return user;
    }
}
