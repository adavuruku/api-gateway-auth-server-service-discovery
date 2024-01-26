package com.example.user_service.controller;

import com.example.user_service.aspects.Loggable;
import com.example.user_service.constants.CommonConstants;
import com.example.user_service.dtos.request.CreateCustomerDto;
import com.example.user_service.dtos.response.UserListResponseDto;
import com.example.user_service.dtos.response.UserResponseDto;
import com.example.user_service.model.Users;
import com.example.user_service.service.UserService;
import com.example.user_service.utils.ValidationLevel;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Loggable
@RestController
@Validated
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @CircuitBreaker(name = "user-service-breaker", fallbackMethod = "usersFallbackMethod")
    @PostMapping(value = "/create/customer", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Validated(ValidationLevel.onCreate.class)
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid CreateCustomerDto createCustomerDto) {
        try {
            Users user = userService.createCustomer(createCustomerDto);

            UserResponseDto respModel = UserResponseDto.builder()
                    .data(user).code(HttpStatus.CREATED.value()).message(
                            CommonConstants.SUCCESSFUL_POST_MESSAGE
                    ).build();
            return new ResponseEntity(respModel, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @CircuitBreaker(name = "user-service-breaker", fallbackMethod = "usersFallbackMethod")
    @PreAuthorize("hasAnyAuthority('REGISTER_USER')")
    @PostMapping(value = "/create/admin", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Validated(ValidationLevel.onAdminCreate.class)
    public ResponseEntity<UserResponseDto> createAdminUser(@RequestBody @Valid CreateCustomerDto createCustomerDto) {
        try {
            Users user = userService.createAdminUsers(createCustomerDto);

            UserResponseDto respModel = UserResponseDto.builder()
                    .data(user).code(HttpStatus.CREATED.value()).message(
                            CommonConstants.SUCCESSFUL_POST_MESSAGE
                    ).build();
            return new ResponseEntity(respModel, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
    @PutMapping(value = "/updatePassword", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Validated(ValidationLevel.onPasswordChange.class)
    public ResponseEntity<UserResponseDto> updatePassword(@RequestBody @Valid  CreateCustomerDto createCustomerDto, Principal principal) {
        createCustomerDto.setEmailAddress(principal.getName());
        Users user = userService.updateMyAccountPassword(createCustomerDto);

        UserResponseDto respModel = UserResponseDto.builder()
                .data(user).code(HttpStatus.OK.value()).message(
                        CommonConstants.SUCCESSFUL_PUT_MESSAGE
                ).build();
        return new ResponseEntity(respModel, HttpStatus.OK);
    }

    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
    @PutMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserResponseDto> updateMyAccountDetails(@RequestBody CreateCustomerDto createCustomerDto, Principal principal) {
        createCustomerDto.setEmailAddress(principal.getName());
        Users user = userService.updateMyAccount(createCustomerDto);

        UserResponseDto respModel = UserResponseDto.builder()
                .data(user).code(HttpStatus.OK.value()).message(
                        CommonConstants.SUCCESSFUL_PUT_MESSAGE
                ).build();
        return new ResponseEntity(respModel, HttpStatus.OK);
    }

    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
    @GetMapping(value = "/admin/{type}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('VIEW_ALL_ADMIN_USERS')")
    public ResponseEntity<UserListResponseDto> getAdminUsers(@PathVariable("type") @NotNull String type) {

        List<Users> usersList = userService.getAllAdmin(type);

        UserListResponseDto respModel = UserListResponseDto.builder()
                .data(usersList).code(HttpStatus.OK.value()).message(
                        CommonConstants.SUCCESSFUL_OPERATION
                ).build();
        return new ResponseEntity(respModel, HttpStatus.OK);
    }

    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
    @GetMapping(value = "/customers", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('VIEW_ALL_CUSTOMER_USERS')")
    public ResponseEntity<UserListResponseDto> getCustomerUsers() {

        List<Users> usersList = userService.getAllCustomers();

        UserListResponseDto respModel = UserListResponseDto.builder()
                .data(usersList).code(HttpStatus.OK.value()).message(
                        CommonConstants.SUCCESSFUL_OPERATION
                ).build();
        return new ResponseEntity(respModel, HttpStatus.OK);
    }

    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
    @DeleteMapping(value = "/delete/my/account", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('DELETE_MY_ACCOUNT')")
    public ResponseEntity<UserResponseDto> deleteMyPersonalAccount(Principal principal) {

        userService.deleteMyPersonalAccount(principal.getName());
        UserResponseDto respModel = UserResponseDto.builder()
                .data(null).code(HttpStatus.NO_CONTENT.value()).message(
                        CommonConstants.SUCCESSFUL_DELETE_MESSAGE
                ).build();
        return new ResponseEntity(respModel, HttpStatus.NO_CONTENT);
    }

    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
    @DeleteMapping(value = "/delete/customer/{emailAddress}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('DELETE_CUSTOMER')")
    public ResponseEntity<UserResponseDto> deleteACustomerAccount(@PathVariable("emailAddress") @NotNull String emailAddress) {

        userService.deleteMyPersonalAccount(emailAddress);
        UserResponseDto respModel = UserResponseDto.builder()
                .data(null).code(HttpStatus.NO_CONTENT.value()).message(
                        CommonConstants.SUCCESSFUL_DELETE_MESSAGE
                ).build();
        return new ResponseEntity(respModel, HttpStatus.NO_CONTENT);
    }
    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
    @DeleteMapping(value = "/delete/admin/{emailAddress}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('DELETE_USER')")
    public ResponseEntity<UserResponseDto> deleteAnAdminAccount(@PathVariable("emailAddress") @NotNull String emailAddress) {

        userService.deleteMyPersonalAccount(emailAddress);
        UserResponseDto respModel = UserResponseDto.builder()
                .data(null).code(HttpStatus.NO_CONTENT.value()).message(
                        CommonConstants.SUCCESSFUL_DELETE_MESSAGE
                ).build();
        return new ResponseEntity(respModel, HttpStatus.NO_CONTENT);
    }

    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
    @PutMapping(value = "/admin/activate-account/{emailAddress}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('ACTIVATE_DEACTIVATE_USER')")
    public ResponseEntity<UserResponseDto> activateUserAccount(@PathVariable("emailAddress") @NotNull String emailAddress) {

        Users user = userService.adminActivateOrDeactivateUsersRecord(true,emailAddress);
        UserResponseDto respModel = UserResponseDto.builder()
                .data(user).code(HttpStatus.OK.value()).message(
                        CommonConstants.SUCCESSFUL_PUT_MESSAGE
                ).build();
        return new ResponseEntity(respModel, HttpStatus.OK);
    }

    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
    @PutMapping(value = "/admin/deactivate-account/{emailAddress}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('ACTIVATE_DEACTIVATE_USER')")
    public ResponseEntity<UserResponseDto> deActivateUserAccount(@PathVariable("emailAddress") @NotNull String emailAddress) {

        Users user = userService.adminActivateOrDeactivateUsersRecord(false,emailAddress);
        UserResponseDto respModel = UserResponseDto.builder()
                .data(user).code(HttpStatus.OK.value()).message(
                        CommonConstants.SUCCESSFUL_PUT_MESSAGE
                ).build();
        return new ResponseEntity(respModel, HttpStatus.OK);
    }

    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
    @PutMapping(value = "/customer/activate-account/{emailAddress}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('ACTIVATE_DEACTIVATE_CUSTOMER')")
    public ResponseEntity<UserResponseDto> activateCustomerAccount(@PathVariable("emailAddress") @NotNull String emailAddress) {

        Users user = userService.activateOrDeactivateCustomerRecord(true,emailAddress);
        UserResponseDto respModel = UserResponseDto.builder()
                .data(user).code(HttpStatus.OK.value()).message(
                        CommonConstants.SUCCESSFUL_PUT_MESSAGE
                ).build();
        return new ResponseEntity(respModel, HttpStatus.OK);
    }

    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
    @PutMapping(value = "/customer/deactivate-account/{emailAddress}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('ACTIVATE_DEACTIVATE_CUSTOMER')")
    public ResponseEntity<UserResponseDto> deActivateCustomerAccount(@PathVariable("emailAddress") @NotNull String emailAddress) {

        Users user = userService.activateOrDeactivateCustomerRecord(false,emailAddress);
        UserResponseDto respModel = UserResponseDto.builder()
                .data(user).code(HttpStatus.OK.value()).message(
                        CommonConstants.SUCCESSFUL_PUT_MESSAGE
                ).build();
        return new ResponseEntity(respModel, HttpStatus.OK);
    }

    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
    @GetMapping(value = "/profile/{emailAddressOrId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserResponseDto> findUserByEmail(@PathVariable("emailAddressOrId") @NotNull String emailAddressOrId) {

        Users user = userService.getByUserNameOrId(emailAddressOrId);
        UserResponseDto respModel = UserResponseDto.builder()
                .data(user).code(HttpStatus.OK.value()).message(
                        CommonConstants.SUCCESSFUL_OPERATION
                ).build();
        return new ResponseEntity(respModel, HttpStatus.OK);
    }

    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserResponseDto> getLoginUser(Principal principal) {

        Users user = userService.getByUserNameOrId(principal.getName());
        UserResponseDto respModel = UserResponseDto.builder()
                .data(user).code(HttpStatus.OK.value()).message(
                        CommonConstants.SUCCESSFUL_OPERATION
                ).build();
        return new ResponseEntity(respModel, HttpStatus.OK);
    }

    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
    @PutMapping(value = "/admin/update-users-role", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('UPDATE_USER')")
    @Validated(ValidationLevel.onAdminUpdateRole.class)
    public ResponseEntity<UserResponseDto> updateUserAsAdminOrModerator(@RequestBody @Valid CreateCustomerDto createCustomerDto) {

        Users user = userService.updateUserAdminAndModeratorStatus(createCustomerDto);
        UserResponseDto respModel = UserResponseDto.builder()
                .data(user).code(HttpStatus.OK.value()).message(
                        CommonConstants.SUCCESSFUL_PUT_MESSAGE
                ).build();
        return new ResponseEntity(respModel, HttpStatus.OK);
    }
    

    public ResponseEntity<UserResponseDto> usersFallbackMethod(CallNotPermittedException ex) {
        UserResponseDto respModel = UserResponseDto.builder()
                .code(HttpStatus.BAD_GATEWAY.value()).message(ex.getMessage()).build();
        return new ResponseEntity(respModel, HttpStatus.BAD_GATEWAY);
    }
}
