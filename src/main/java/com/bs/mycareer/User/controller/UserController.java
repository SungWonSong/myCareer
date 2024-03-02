package com.bs.mycareer.User.controller;

import com.bs.mycareer.User.dto.RegisterRequest;
import com.bs.mycareer.User.service.UserService;
import com.bs.mycareer.Common.exceptions.CustomException;
import com.bs.mycareer.Common.exceptions.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {


    @Autowired
    private final UserService userService;



    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @RequestBody를 통해 보내고, 지금은 responsebody 응답값을 보여주지만 (혹은 error) -> login page를 보여주기로 할 예정
    // exception 추후에 만들어서 넣을 예정
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.registerProcess(registerRequest);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseCode.SUCCESS_SIGNUP.getDetail());
        }catch (CustomException e) {
            // CustomException이 발생하면 해당 예외의 상세 메시지를 클라이언트에게 전달
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
//        try {
//            userService.logoutProcess(httpServletRequest,httpServletResponse);
//            return ResponseEntity.ok("Logout successful");
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}

//    @PostMapping()
//    @PostMapping("/login")
//    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
//        userService.registerProcess(registerRequest);
//        return ResponseEntity.ok("Registration successful");
//    }
