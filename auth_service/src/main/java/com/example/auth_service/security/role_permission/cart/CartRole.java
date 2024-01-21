package com.example.auth_service.security.role_permission.cart;

import com.example.auth_service.security.role_permission.transaction.TransactionPermission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum CartRole {
    GUEST(new HashSet<>()),
    CUSTOMERUSER(new HashSet<>(Arrays.asList(CartPermission.CHECKOUT_CART,
            CartPermission.ADD_TO_CART, CartPermission.DELETE_FROM_CART, CartPermission.VIEW_CART))),
    MODERATOR(new HashSet<>(Arrays.asList(CartPermission.VIEW_CART))),
    ADMIN(new HashSet<>(Arrays.asList(CartPermission.UPDATE_CART_ITEM_AFTER_CHECKOUT,
            CartPermission.DELETE_CART_ITEM_AFTER_CHECKOUT)));

    Set<CartPermission> permissions;

    CartRole(Set<CartPermission> permissions) {
        this.permissions=permissions;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return this.permissions.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toSet());
    }

}