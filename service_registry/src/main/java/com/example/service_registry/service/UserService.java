package com.example.service_registry.service;

import com.example.service_registry.dtos.RoleDto;
import com.example.service_registry.dtos.UserDto;
import com.example.service_registry.entity.Role;
import com.example.service_registry.entity.User;
import com.example.service_registry.repository.RoleRepository;
import com.example.service_registry.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public User createUser(UserDto createUser){
        List<Role> role = roleRepository.findByNameIn(createUser.getRoles());
        Set<Role> roleSet = new HashSet<Role>(role);
        String password = passwordEncoder.encode(createUser.getPassword());
        User user = User.builder()
                .password(password)
                .fullName(createUser.getFullName())
                .email(createUser.getEmail())
                .userName(createUser.getUserName())
                .roles(roleSet).build();

        return userRepository.save(user);
    }

    public Role createRole(RoleDto roleDto){
        Role role = Role.builder()
                .name(roleDto.getRoleName()).build();

        return roleRepository.save(role);
    }
}
