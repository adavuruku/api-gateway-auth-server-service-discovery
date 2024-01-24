package com.example.auth_service.security;



import com.example.auth_service.model.Users;

import com.example.auth_service.security.role_permission.book.BookRole;
import com.example.auth_service.security.role_permission.cart.CartRole;
import com.example.auth_service.security.role_permission.transaction.TransactionRole;
import com.example.auth_service.security.role_permission.user.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@ToString
@RequiredArgsConstructor
public class ApplicationUsers implements UserDetails {

    private final Users userObject;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> allAuthority = new HashSet<>();
        if(userObject.getIsAdmin().equalsIgnoreCase("Y")) {
            allAuthority = UserRole.ADMIN.getAuthorities();

            Set<GrantedAuthority> bookAuthority = BookRole.ADMIN.getAuthorities();
            allAuthority.addAll(bookAuthority);

            bookAuthority = TransactionRole.ADMIN.getAuthorities();
            allAuthority.addAll(bookAuthority);

            bookAuthority = CartRole.ADMIN.getAuthorities();
            allAuthority.addAll(bookAuthority);
        }
        if(userObject.getIsModerator().equalsIgnoreCase("Y")) {
            allAuthority = UserRole.MODERATOR.getAuthorities();

            Set<GrantedAuthority> bookAuthority = BookRole.MODERATOR.getAuthorities();
            allAuthority.addAll(bookAuthority);

            bookAuthority = TransactionRole.MODERATOR.getAuthorities();
            allAuthority.addAll(bookAuthority);

            bookAuthority = CartRole.MODERATOR.getAuthorities();
            allAuthority.addAll(bookAuthority);
        }

        if(userObject.getIsCustomerUser().equalsIgnoreCase("Y")) {
            allAuthority = UserRole.CUSTOMERUSER.getAuthorities();

            Set<GrantedAuthority> bookAuthority = BookRole.CUSTOMERUSER.getAuthorities();
            allAuthority.addAll(bookAuthority);

            bookAuthority = TransactionRole.CUSTOMERUSER.getAuthorities();
            allAuthority.addAll(bookAuthority);

            bookAuthority = CartRole.CUSTOMERUSER.getAuthorities();
            allAuthority.addAll(bookAuthority);


        }
        try {
            log.info(String.format("Authorities => {}", new ObjectMapper().writeValueAsString(allAuthority)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return allAuthority;
    }

    @Override
    public String getPassword() {
        log.info(String.format("User: %s - Password: %s", userObject.getEmailAddress(), userObject.getPassword()));
        return userObject.getPassword();
    }

    @Override
    public String getUsername() {
        return userObject.getEmailAddress();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !userObject.isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return userObject.isAccountDeleted();
    }
}