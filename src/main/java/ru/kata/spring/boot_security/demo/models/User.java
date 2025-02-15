package ru.kata.spring.boot_security.demo.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Name shouldn't be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @Column(name = "username")
    private String username;
    @Min(value = 0, message = "Age should be greater than 0")
    @Max(value = 120, message = "Age should be valid")
    @Column(name = "age")
    private int age;
    @NotEmpty(message = "Email shouldn't be empty")
    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;
    @NotEmpty
    @Size(min = 2, max = 100, message = "Длина пароля должна быть от 2 до 300000 символов")
    @Column(name = "password", length = 100)
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    public User() {}

    public User(String username, String email, String password, List<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public void setUsername(@NotEmpty(message = "Name shouldn't be empty") @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters") String username) {
        this.username = username;
    }

    @Min(value = 0, message = "Age should be greater than 0")
    @Max(value = 120, message = "Age should be valid")
    public int getAge() {
        return age;
    }

    public void setAge(@Min(value = 0, message = "Age should be greater than 0") @Max(value = 120, message = "Age should be valid") int age) {
        this.age = age;
    }

    public @NotEmpty(message = "Email shouldn't be empty") @Email(message = "Email should be valid") String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "Email shouldn't be empty") @Email(message = "Email should be valid") String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public List<Role> getRole() {
        return roles;
    }

    public void setRole(List<Role> role) {
        this.roles = role;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return  Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}
