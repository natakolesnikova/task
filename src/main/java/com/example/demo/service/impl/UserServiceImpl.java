package com.example.demo.service.impl;

import com.example.demo.dto.UserDTO;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.persistance.entity.User;
import com.example.demo.persistance.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO createNewUserInstance(UserDTO userDTO) throws UserAlreadyExistsException {
        UserDTO createdUser = new UserDTO();
        User existingUser = userRepository.findUserByUserName(userDTO.getUserName());

        if (nonNull(existingUser)) {
            throw new UserAlreadyExistsException();
        } else {
            User user = saveValidUserInstanceToDB(userDTO);
            convertUserModelToDTO(createdUser, user);
        }
        return createdUser;
    }

    private void convertUserModelToDTO(UserDTO createdUser, User user) {
        createdUser.setFirstName(user.getFirstName());
        createdUser.setId(user.getId());
        createdUser.setLastName(user.getLastName());
        createdUser.setUserName(user.getUserName());
    }

    private User saveValidUserInstanceToDB(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setUserName(userDTO.getUserName());
        userRepository.save(user);
        return user;
    }
}
