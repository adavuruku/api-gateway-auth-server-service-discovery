package com.example.user_service.service;

import com.example.user_service.dao.UsersRepository;
import com.example.user_service.dtos.request.CreateCustomerDto;
import com.example.user_service.model.Users;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@AllArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    public Users createCustomer(CreateCustomerDto createCustomer){
        Users newCustomers = Users.builder()
                .firstName(createCustomer.getFirstName())
                .lastName(createCustomer.getLastName())
                .phoneNumber(createCustomer.getPhoneNumber())
                .password(bCryptPasswordEncoder.encode(createCustomer.getPassword()))
                .profileImage(createCustomer.getProfileImage())
                .emailAddress(createCustomer.getEmailAddress())
                .contactAddress(createCustomer.getContactAddress()).build();

        return newCustomers;

    }

    @Cacheable(value="user", key = "#emailAddress", unless="#result == null")
    public Optional<Users> getByUsrName(String emailAddress) {
        return usersRepository.findByEmailAddress(emailAddress);
    }

}
