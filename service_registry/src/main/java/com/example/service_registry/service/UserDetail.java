package com.example.service_registry.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.service_registry.entity.User;
import com.example.service_registry.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetail implements UserDetailsService {
    @Autowired
    UserRepository userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByUserNameOrEmail(username, username);
        if(!userOptional.isPresent()){
            throw new UsernameNotFoundException("User not exists by Username");
        }
        User user = userOptional.get();
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
    }
}