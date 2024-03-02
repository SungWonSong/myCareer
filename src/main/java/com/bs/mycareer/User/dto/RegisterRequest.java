package com.bs.mycareer.User.dto;


import com.bs.mycareer.User.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    private String email;
    private String password;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .role("USER")
                .build();
    }


}
