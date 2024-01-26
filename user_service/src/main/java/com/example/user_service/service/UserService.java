package com.example.user_service.service;

import com.example.user_service.dao.UsersRepository;
import com.example.user_service.dtos.request.CreateCustomerDto;
import com.example.user_service.exceptions.PasswordMissMatchException;
import com.example.user_service.exceptions.ResourceNotFoundException;
import com.example.user_service.model.Users;
import com.mongodb.DuplicateKeyException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
//todo -> admin/moderator get all deleted accounts group by types [customers, admin, moderator]
//    todo -> admin/moderator get all deactivated account group by types [customers, admin, moderator]
    private final UsersRepository usersRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    public Users createCustomer(CreateCustomerDto createCustomer) {
//        Users newCustomers = Users.builder()
//                .firstName(createCustomer.getFirstName())
//                .lastName(createCustomer.getLastName())
//                .phoneNumber(createCustomer.getPhoneNumber())
//                .password(bCryptPasswordEncoder.encode(createCustomer.getPassword()))
//                .profileImage(createCustomer.getProfileImage())
//                .emailAddress(createCustomer.getEmailAddress())
//                .contactAddress(createCustomer.getContactAddress()).build();
        try {
            Users newCustomers = new Users();
            newCustomers.setFirstName(createCustomer.getFirstName());
            newCustomers.setLastName(createCustomer.getLastName());
            newCustomers.setPhoneNumber(createCustomer.getPhoneNumber());
            newCustomers.setPassword(bCryptPasswordEncoder.encode(createCustomer.getPassword()));
            newCustomers.setProfileImage(createCustomer.getProfileImage());
            newCustomers.setEmailAddress(createCustomer.getEmailAddress());
            newCustomers.setContactAddress(createCustomer.getContactAddress());

            return usersRepository.save(newCustomers);
        } catch (Exception ex) {
            if (ex instanceof DuplicateKeyException) {
                throw new com.example.user_service.exceptions.DuplicateKeyException(
                        ex.getMessage(), null
                );
            } else {
                throw ex;
            }
        }

    }

    @Cacheable(value = "user", key = "#emailAddress", unless = "#result == null")
    public Users getByUserName(String emailAddress) {
        try {
            Map<String, String> error = new HashMap<>();
            error.put("Username", emailAddress);
            return usersRepository.findByEmailAddressAndIsAccountDeleted(emailAddress, false).orElseThrow(
                    () -> new ResourceNotFoundException("User doesn't exist", error));
        } catch (ResourceNotFoundException ex) {
            throw ex;
        }
    }

    @Cacheable(value = "user", key = "#id", unless = "#result == null")
    public Users getById(String id) {
        try {
            Map<String, String> error = new HashMap<>();
            error.put("userId", id);
            return usersRepository.findByIdAndIsAccountDeleted(id, false).orElseThrow(
                    () -> new ResourceNotFoundException("User doesn't exist", error));
        } catch (ResourceNotFoundException ex) {
            throw ex;
        }
    }

    public Users createAdminUsers(CreateCustomerDto createCustomer) {
        try {
            Users newCustomers = new Users();
            newCustomers.setFirstName(createCustomer.getFirstName());
            newCustomers.setLastName(createCustomer.getLastName());
            newCustomers.setPhoneNumber(createCustomer.getPhoneNumber());
            newCustomers.setPassword(bCryptPasswordEncoder.encode(createCustomer.getPassword()));
            newCustomers.setProfileImage(createCustomer.getProfileImage());
            newCustomers.setEmailAddress(createCustomer.getEmailAddress());
            newCustomers.setContactAddress(createCustomer.getContactAddress());
            newCustomers.setIsCustomerUser("N");
            newCustomers.setIsModerator(createCustomer.getIsModerator());
            newCustomers.setIsAdmin(createCustomer.getIsAdmin());

            return usersRepository.save(newCustomers);
        } catch (Exception ex) {
            if (ex instanceof DuplicateKeyException) {
                throw new com.example.user_service.exceptions.DuplicateKeyException(
                        ex.getMessage(), null
                );
            } else {
                throw ex;
            }
        }
    }

    public Users updateUserAdminAndModeratorStatus(CreateCustomerDto createCustomer) {
        try {
            Map<String, String> error = new HashMap<>();
            error.put("Email", createCustomer.getEmailAddress());

            Users userRecord = usersRepository.findByEmailAddress(createCustomer.getEmailAddress()).orElseThrow(
                    () -> new ResourceNotFoundException("User doesn't exist", error));

            userRecord.setIsModerator(createCustomer.getIsModerator());
            userRecord.setIsModerator(createCustomer.getIsAdmin());
            userRecord.setIsCustomerUser("N");

            usersRepository.save(userRecord);

            return userRecord;

        } catch (ResourceNotFoundException ex) {
            throw ex;
        }
    }

    public Users adminDeactivateUsersRecord(CreateCustomerDto createCustomer) {
        try {
            Map<String, String> error = new HashMap<>();
            error.put("Email", createCustomer.getEmailAddress());

            Users userRecord = usersRepository.findByEmailAddress(createCustomer.getEmailAddress()).orElseThrow(
                    () -> new ResourceNotFoundException("User doesn't exist", error));
            userRecord.setAccountLocked(true);
            usersRepository.save(userRecord);

            return userRecord;

        } catch (ResourceNotFoundException ex) {
            throw ex;
        }
    }

    public Users adminActivateUsersRecord(CreateCustomerDto createCustomer) {
        try {
            Map<String, String> error = new HashMap<>();
            error.put("Email", createCustomer.getEmailAddress());

            Users userRecord = usersRepository.findByEmailAddress(createCustomer.getEmailAddress()).orElseThrow(
                    () -> new ResourceNotFoundException("User doesn't exist", error));
            userRecord.setAccountLocked(false);
            usersRepository.save(userRecord);

            return userRecord;

        } catch (ResourceNotFoundException ex) {
            throw ex;
        }
    }

    public Users adminDeleteUsersRecord(CreateCustomerDto createCustomer) {
        try {
            Map<String, String> error = new HashMap<>();
            error.put("Email", createCustomer.getEmailAddress());

            Users userRecord = usersRepository.findByEmailAddress(createCustomer.getEmailAddress()).orElseThrow(
                    () -> new ResourceNotFoundException("User doesn't exist", error));
            userRecord.setAccountDeleted(true);
            userRecord.setEmailAddress(userRecord.getId().toString().concat("_").concat(userRecord.getEmailAddress()));
            usersRepository.save(userRecord);

            return userRecord;

        } catch (ResourceNotFoundException ex) {
            throw ex;
        }
    }

    public Users deleteMyPersonalAccount(String emailAddress) {
        try {
            Map<String, String> error = new HashMap<>();
            error.put("Email", emailAddress);

            Users userRecord = usersRepository.findByEmailAddress(emailAddress).orElseThrow(
                    () -> new ResourceNotFoundException("User doesn't exist", error));
            userRecord.setAccountDeleted(true);
            userRecord.setEmailAddress(userRecord.getId().toString().concat("_").concat(userRecord.getEmailAddress()));
            usersRepository.save(userRecord);

            return userRecord;

        } catch (ResourceNotFoundException ex) {
            throw ex;
        }
    }

    public List<Users> getAllCustomers() {
        try {

            List<Users> userRecord = usersRepository.findByIsAccountLockedIsAccountDeletedIsCustomer(false, false, "Y");
            return userRecord;

        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    public List<Users> getAllAdmin(String type) {
        try {
            List<Users> userRecord = new ArrayList<>();
            if (type.equalsIgnoreCase("admin")) {
                userRecord = usersRepository.findByIsAccountLockedIsAccountDeletedIsAdmin(false, false, "Y");
            } else if (type.equalsIgnoreCase("moderator")) {
                userRecord = usersRepository.findByIsAccountLockedIsAccountDeletedIsModerator(false, false, "Y");
            }
            return userRecord;
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    public Users updateMyAccountPassword(CreateCustomerDto createCustomerDto) {
        try {
            Map<String, String> error = new HashMap<>();
            error.put("Email", createCustomerDto.getEmailAddress());

            Users userRecord = usersRepository.findByEmailAddress(createCustomerDto.getEmailAddress()).orElseThrow(
                    () -> new ResourceNotFoundException("User doesn't exist", error));
            String hashPassword = bCryptPasswordEncoder.encode(createCustomerDto.getPassword());
            if (!userRecord.getPassword().equalsIgnoreCase(hashPassword)) {
                throw new PasswordMissMatchException("Invalid previous / existing password provided!", null);
            }
            userRecord.setPassword(bCryptPasswordEncoder.encode(createCustomerDto.getNewPassword()));
            usersRepository.save(userRecord);

            return userRecord;

        } catch (ResourceNotFoundException | PasswordMissMatchException ex) {
            throw ex;
        }
    }

    //not yet completed [contactAddress profileImage phoneNumber]
    public Users updateMyAccount(CreateCustomerDto createCustomerDto) {
        try {
            Map<String, String> error = new HashMap<>();
            error.put("Email", createCustomerDto.getEmailAddress());

            Users userRecord = usersRepository.findByEmailAddressAndIsAccountDeletedAndIsAccountLocked(createCustomerDto.getEmailAddress(), false, false).orElseThrow(
                    () -> new ResourceNotFoundException("User doesn't exist", error));

            userRecord.setContactAddress(
                    createCustomerDto.getContactAddress() == null ? userRecord.getContactAddress() : createCustomerDto.getContactAddress()
            );
            userRecord.setProfileImage(
                    createCustomerDto.getProfileImage() == null ? userRecord.getProfileImage() : createCustomerDto.getProfileImage()
            );
            userRecord.setPhoneNumber(
                    createCustomerDto.getPhoneNumber() == null ? userRecord.getPhoneNumber() : createCustomerDto.getPhoneNumber()
            );
            usersRepository.save(userRecord);

            return userRecord;

        } catch (ResourceNotFoundException | PasswordMissMatchException ex) {
            throw ex;
        }
    }
}
