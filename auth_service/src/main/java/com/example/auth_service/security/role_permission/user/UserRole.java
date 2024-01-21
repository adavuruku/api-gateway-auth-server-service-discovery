package com.example.auth_service.security.role_permission.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum UserRole {
    GUEST(new HashSet<>()),
    CUSTOMERUSER(new HashSet<>(Arrays.asList(UserPermission.UPDATE_MY_ACCOUNT, UserPermission.DELETE_MY_ACCOUNT))),
    MODERATOR(new HashSet<>(Arrays.asList(UserPermission.ACTIVATE_DEACTIVATE_CUSTOMER,
            UserPermission.VIEW_ALL_USERS, UserPermission.UPDATE_MY_ACCOUNT))),
    ADMIN(new HashSet<>(Arrays.asList(UserPermission.ACTIVATE_DEACTIVATE_CUSTOMER,
            UserPermission.VIEW_ALL_USERS, UserPermission.UPDATE_MY_ACCOUNT, UserPermission.DELETE_MY_ACCOUNT,
            UserPermission.ACTIVATE_DEACTIVATE_USER, UserPermission.UPDATE_USER, UserPermission.REGISTER_USER,
            UserPermission.DELETE_USER, UserPermission.DELETE_CUSTOMER)));

    Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions=permissions;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return this.permissions.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toSet());
    }

}