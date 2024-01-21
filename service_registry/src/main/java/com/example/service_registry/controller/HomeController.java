package com.example.service_registry.controller;

import com.example.service_registry.dtos.RoleDto;
import com.example.service_registry.dtos.UserDto;
import com.example.service_registry.entity.Role;
import com.example.service_registry.entity.User;
import com.example.service_registry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }
    @PostMapping("/role")
    public ResponseEntity<Role> createRole(@RequestBody RoleDto roleDto) {
        return ResponseEntity.ok(userService.createRole(roleDto));
    }
}