package com.bs.mycareer.controller;

import com.bs.mycareer.dto.UserRegisterDto;
import com.bs.mycareer.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserServiceImpl userServiceimpl;

    @Autowired
    public UserController(UserServiceImpl userServiceimpl){

        this.userServiceimpl= userServiceimpl;
    }

    //@RequestParam은 메소드 사용과 매개변수랑 의존관계형성 즉 매핑해주는 것이다.
    @PostMapping("/register")
    public String registerProcess(@RequestParam UserRegisterDto userRegisterDto){
        userServiceimpl.registerProcess(userRegisterDto);
        return "login";
    }
}
