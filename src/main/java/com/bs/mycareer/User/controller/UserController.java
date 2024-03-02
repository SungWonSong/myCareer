package com.bs.mycareer.User.controller;

import com.bs.mycareer.User.dto.RegisterRequest;
import com.bs.mycareer.User.service.UserService;
import com.bs.mycareer.Common.exceptions.CustomException;
import com.bs.mycareer.Common.exceptions.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class UserController {


    @Autowired
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @RequestBody를 통해 보내고, 지금은 responsebody 응답값을 보여주지만 (혹은 error) -> login page를 보여주기로 할 예정
    // exception 추후에 만들어서 넣을 예정
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
//        try {
//            userService.registerProcess(registerRequest);
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(ResponseCode.SUCCESS_SIGNUP.getDetail());
//        }catch (CustomException e) {
//            // CustomException이 발생하면 해당 예외의 상세 메시지를 클라이언트에게 전달
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
    @GetMapping("/register")
    public String registerPage(Model model, RegisterRequest registerRequest) {
        model.addAttribute("registerRequest", registerRequest); // 빈 객체 추가
        model.addAttribute("loginRequest", registerRequest); // 빈 객체 추가
        return "login";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerRequest") RegisterRequest registerRequest, Model model) {
        System.out.println("registerRequest.getEmail() = " + registerRequest.getEmail());
        System.out.println("registerRequest.getPassword() = " + registerRequest.getPassword());
        try {
            // 회원가입 로직 처리
            userService.registerProcess(registerRequest);
            model.addAttribute("message", ResponseCode.SUCCESS_SIGNUP.getDetail());
        } catch (CustomException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/register"; // login.html로 이동
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

    //    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody RegisterRequest registerRequest) {
//        userService.registerProcess(registerRequest);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(ResponseCode.SUCCESS_SIGNIN.getDetail());
//    }
//    @GetMapping("/login")
//    public String loginPage(Model model) {
//        model.addAttribute("loginRequest", new RegisterRequest()); // 빈 객체 추가
//        return "login";
//    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginRequest") RegisterRequest loginRequest, Model model) {
        System.out.println("loginRequest.getEmail() = " + loginRequest.getEmail());
        System.out.println("loginRequest.getPassword() = " + loginRequest.getPassword());
        try {
            userService.registerProcess(loginRequest);
            model.addAttribute("message", ResponseCode.SUCCESS_SIGNIN.getDetail());
        } catch (CustomException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/";
    }

}
