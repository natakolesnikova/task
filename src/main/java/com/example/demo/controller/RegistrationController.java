package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("userservice")
public class RegistrationController {

    private static final String ID = "id";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String USER_NAME = "userName";
    private static final String CODE = "code";
    private static final String DESCRIPTION = "description";
    private static final String REGISTER = "/register";
    private static final String LOGIN = "/login";
    private static final String PASSWORD = "password";
    private static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    private static final String A_USER_WITH_THE_GIVEN_USERNAME_ALREADY_EXISTS = "A user with the given username already exists";

    @Autowired
    private UserService userService;

    @PostMapping(value = REGISTER)
    public ResponseEntity<Map<String,String>> registerNewUserInstance(@RequestBody UserDTO userDTO) {
        try {
            UserDTO newUserInstance = userService.createNewUserInstance(userDTO);
            Map<String, String> response = new HashMap<String, String>();
            response.put(ID, String.valueOf(newUserInstance.getId()));
            response.put(FIRST_NAME, newUserInstance.getFirstName());
            response.put(LAST_NAME, newUserInstance.getLastName());
            response.put(USER_NAME, newUserInstance.getUserName());
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            Map<String, String> response = new HashMap<String, String>();
            response.put(CODE, USER_ALREADY_EXISTS);
            response.put(DESCRIPTION, A_USER_WITH_THE_GIVEN_USERNAME_ALREADY_EXISTS);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = LOGIN, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> login(@RequestParam(value = USER_NAME) String userName, @RequestParam(value = PASSWORD) String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userName, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        Map<String, String> response = new HashMap<String, String>();
        response.put(ID, String.valueOf(principal.getId()));
        response.put(USER_NAME, principal.getUsername());
        response.put(FIRST_NAME, principal.getFirstName());
        response.put(LAST_NAME, principal.getLastName());

        return ResponseEntity.ok(response);
    }
}