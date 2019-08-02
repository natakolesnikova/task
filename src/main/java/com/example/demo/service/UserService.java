package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.exception.UserAlreadyExistsException;

public interface UserService {

    UserDTO createNewUserInstance(UserDTO userDTO) throws UserAlreadyExistsException;
}
