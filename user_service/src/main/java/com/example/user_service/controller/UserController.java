package com.example.user_service.controller;

import com.example.user_service.aspects.Loggable;
import com.example.user_service.constants.CommonConstants;
import com.example.user_service.dtos.request.CreateCustomerDto;
import com.example.user_service.dtos.response.CreateUserResponseDto;
import com.example.user_service.model.Users;
import com.example.user_service.service.UserService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Loggable
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
//    @PreAuthorize("hasAnyAuthority('USER_WRITE')")
    @PostMapping(value = "/create/customer", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CreateUserResponseDto> createUser(@Validated @RequestBody CreateCustomerDto createCustomerDto) {
        try {
            Users user = userService.createCustomer(createCustomerDto);

            CreateUserResponseDto respModel = CreateUserResponseDto.builder()
                    .data(user).code(HttpStatus.CREATED.value()).message(
                            CommonConstants.SUCCESSFUL_POST_MESSAGE
                    ).build();
            return new ResponseEntity(respModel, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
//    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<EntityModel<UsersResponseModel>> getUsers() {
//        UsersResponseModel respModel = new UsersResponseModel();
//        List<Users> usersLst = usersSvc.getUsersList();
//        respModel.setUsersLst(usersLst);
//        respModel.setCode(HttpStatus.OK.value());
//
//        EntityModel<UsersResponseModel> entity = EntityModel.of(respModel);
//        entity = usersProcess.generateHateoas(entity, this, "all-users", null);
//        return new ResponseEntity(entity, HttpStatus.OK);
//    }
//
//    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
//    @PreAuthorize("hasAnyAuthority('USER_READ', 'USER')")
//    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<EntityModel<UsersResponseModel>> getUserById(@PathVariable("id") String id) {
//        UsersResponseModel respModel = new UsersResponseModel();
//        Optional<Users> user = usersSvc.findById(id);
//        if(!user.isPresent()) {
//            throw new RuntimeException();
////            throw new UserExceptions.UserNotFoudException(ErrorsMappings.USER_NOT_FOUND_MESSAGE);
//        }
//        respModel.setUser(user.get());
//        respModel.setCode(HttpStatus.OK.value());
//
//        EntityModel<UsersResponseModel> entity = EntityModel.of(respModel);
//        entity = usersProcess.generateHateoas(entity, this, "view-user", user.get().getId());
//        return new ResponseEntity(entity, HttpStatus.OK);
//    }
//
//
//    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
//    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<EntityModel<UsersResponseModel>> updateUserById(@PathVariable("id") String id,
//                                                                          @Validated @RequestBody UsersRequestModel usersRequest) {
//        UsersResponseModel respModel = new UsersResponseModel();
//        Users user = usersProcess.updateDetails(id, usersRequest);
//
//        EntityModel<UsersResponseModel> entity = EntityModel.of(respModel);
//        entity = usersProcess.generateHateoas(entity, this, "update-user", user.getId());
//        return new ResponseEntity(entity, HttpStatus.OK);
//    }
//
//    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
//    @PutMapping(value = "/{id}/updatePassword", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<EntityModel<UsersResponseModel>> updatePassword(@PathVariable("id") String id,
//                                                                          @Validated @RequestBody UsersRequestModel usersRequest) {
//        UsersResponseModel respModel = new UsersResponseModel();
//        Users user = usersProcess.updatePasswordDetails(id, usersRequest);
//
//        EntityModel<UsersResponseModel> entity = EntityModel.of(respModel);
//        entity = usersProcess.generateHateoas(entity, this, "update-password", user.getId());
//        return new ResponseEntity(entity, HttpStatus.OK);
//    }
//
//    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
//    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<EntityModel<UsersResponseModel>> deleteUserById(@PathVariable("id") String id) {
//        UsersResponseModel respModel = new UsersResponseModel();
//        Optional<Users> user = usersSvc.findById(id);
//        if(!user.isPresent()) {
//            throw new RuntimeException();
////            throw new UserExceptions.UserNotFoudException(ErrorsMappings.USER_NOT_FOUND_MESSAGE);
//        }
//        Users userData = user.get();
//        userData.setDeletedFlag("Y");
//        usersSvc.deleteEntry(userData);
//        respModel.setCode(HttpStatus.OK.value());
//
//        EntityModel<UsersResponseModel> entity = EntityModel.of(respModel);
//        entity = usersProcess.generateHateoas(entity, this, "delete-user", null);
//        return new ResponseEntity(entity, HttpStatus.OK);
//    }
//
//
//    @CircuitBreaker(name = "usersBreaker", fallbackMethod = "usersFallbackMethod")
//    @PostMapping(value = "/authenticate")
//    public ResponseEntity<EntityModel<UsersResponseModel>> authenticateUser(@Validated @RequestBody UsersRequestModel usersRequest) {
//        UsersResponseModel respModel = new UsersResponseModel();
//        Users user = usersProcess.validateUserLogin(usersRequest);
//        respModel.setUser(user);
//        respModel.setCode(HttpStatus.OK.value());
//        EntityModel<UsersResponseModel> entity = EntityModel.of(respModel);
////		entity = usersProcess.generateHateoas(entity, this, "authenticate", user.getId());
//        String token = usersProcess.generateToken(user);
//        return ResponseEntity.ok().header(SecurityConstants.HEADER, token).body(entity);
////        return ResponseEntity.ok().header("Authorization", token).body(entity);
//    }


    public ResponseEntity<CreateUserResponseDto> usersFallbackMethod(CallNotPermittedException ex) {
        CreateUserResponseDto respModel = CreateUserResponseDto.builder()
                .code(HttpStatus.BAD_GATEWAY.value()).message(ex.getMessage()).build();
        return new ResponseEntity(respModel, HttpStatus.BAD_GATEWAY);
    }
}
