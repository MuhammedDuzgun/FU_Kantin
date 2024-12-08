package com.gundemgaming.fukantin.service;

import com.gundemgaming.fukantin.dto.user.CreateUserRequest;
import com.gundemgaming.fukantin.dto.user.UpdateUserRequest;
import com.gundemgaming.fukantin.dto.user.UserResponse;

public interface IUserService {

    UserResponse addUser(CreateUserRequest createUserRequest);

    UserResponse getUserById(Long userId);

    UserResponse updateUser(Long userId, UpdateUserRequest updateUserRequest);

    void deleteUser(Long userId);

}
