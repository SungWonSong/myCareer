package com.bs.mycareer.dto;


import lombok.Getter;
import lombok.Setter;

// user dto 굳이 할 필요가 있나 일단 의문...
@Getter
@Setter
public class UserRegisterDto {
    private String email;
    private String password;
}
