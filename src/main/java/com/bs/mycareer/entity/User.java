package com.bs.mycareer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "\"User\"") //데이터베이스에서 테이블 이름을 User라고 하게 되면 예약어이기 때문에 충돌이 나서 테이블명을 저렇게 감싸주면 사용가능하다!
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

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

    public User() {

    }

    public void addCareer(Career career) {
        careers.add(career);
        career.setUser(this);
    }


}
