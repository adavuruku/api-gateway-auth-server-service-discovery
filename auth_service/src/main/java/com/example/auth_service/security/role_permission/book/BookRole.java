package com.example.auth_service.security.role_permission.book;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum BookRole {
    GUEST(new HashSet<>()),
    CUSTOMERUSER(new HashSet<>(Arrays.asList(BookPermission.OPEN_BOOK))),
    MODERATOR(new HashSet<>(Arrays.asList(BookPermission.OPEN_BOOK, BookPermission.CREATE_BOOK,
            BookPermission.DELETE_BOOK,  BookPermission.UPDATE_BOOK))),
    ADMIN(new HashSet<>(Arrays.asList(BookPermission.OPEN_BOOK, BookPermission.CREATE_BOOK,
            BookPermission.DELETE_BOOK,  BookPermission.UPDATE_BOOK)));

    Set<BookPermission> permissions;

    BookRole(Set<BookPermission> permissions) {
        this.permissions=permissions;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return this.permissions.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toSet());
    }

}