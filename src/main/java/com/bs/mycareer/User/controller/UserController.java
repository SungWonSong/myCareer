package com.bs.mycareer.User.controller;

import com.bs.mycareer.User.dto.RegisterRequest;
import com.bs.mycareer.User.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class UserController {


    @Autowired
    private final UserService userService;

    private AuthenticationManager authenticationManager;


    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    // @RequestBody를 통해 보내고, 지금은 responsebody 응답값을 보여주지만 (혹은 error) -> login page를 보여주기로 할 예정
    // exception 추후에 만들어서 넣을 예정
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.registerProcess(registerRequest);
            return ResponseEntity.ok("Registration successful");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}

//    @PostMapping()
//    @PostMapping("/login")
//    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
//        userService.registerProcess(registerRequest);
//        return ResponseEntity.ok("Registration successful");
//    }
