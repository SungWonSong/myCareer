package com.bs.mycareer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private String email;

    private String password;

    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Career> careers = new ArrayList<>();

    //init할때 필요... 회원가입일경우엔 role -> "USER"를 넣어서 회원가입, 하지만 초기화할때에는 role값에 넣어주는게 없기에 밑의 함수로 role값 -> "USER" 생성
    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }


}
