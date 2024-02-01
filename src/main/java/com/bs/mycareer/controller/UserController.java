package com.bs.mycareer.controller;

import com.bs.mycareer.dto.RegisterRequest;
import com.bs.mycareer.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserServiceImpl userServiceimpl;

    @Autowired
    public UserController(UserServiceImpl userServiceimpl) {

        this.userServiceimpl = userServiceimpl;
    }

    //@RequestParam은 메소드 사용과 매개변수랑 의존관계형성 즉 매핑해주는 것이다.
    // return "login"은 추후 수정 -> login.index 페이지로 리다이렉팅 시도
    @PostMapping("/register")
    public String register(@RequestParam RegisterRequest RegisterDto) {
        userServiceimpl.registerProcess(RegisterDto);
        return "login";
    }

    @GetMapping("/user")
    public String userP(){

        return "usercontroller";
    }
}
