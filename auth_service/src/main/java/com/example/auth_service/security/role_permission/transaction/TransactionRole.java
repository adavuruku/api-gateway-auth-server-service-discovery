package com.example.auth_service.security.role_permission.transaction;

import com.example.auth_service.security.role_permission.user.UserPermission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum TransactionRole {
    GUEST(new HashSet<>()),
    CUSTOMERUSER(new HashSet<>(Arrays.asList(TransactionPermission.ADD_TRANSACTION,
            TransactionPermission.VIEW_MY_TRANSACTION_HISTORY, TransactionPermission.OPEN_TRANSACTION))),
    MODERATOR(new HashSet<>(Arrays.asList(TransactionPermission.ADD_TRANSACTION,
            TransactionPermission.VIEW_MY_TRANSACTION_HISTORY, TransactionPermission.OPEN_TRANSACTION,
            TransactionPermission.VIEW_ANY_USER_TRANSACTION_HISTORY))),
    ADMIN(new HashSet<>(Arrays.asList(TransactionPermission.ADD_TRANSACTION,
            TransactionPermission.VIEW_MY_TRANSACTION_HISTORY, TransactionPermission.OPEN_TRANSACTION,
            TransactionPermission.UPDATE_TRANSACTION, TransactionPermission.VIEW_ANY_USER_TRANSACTION_HISTORY,
            TransactionPermission.DELETE_TRANSACTION)));

    Set<TransactionPermission> permissions;

    TransactionRole(Set<TransactionPermission> permissions) {
        this.permissions=permissions;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return this.permissions.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toSet());
    }

}