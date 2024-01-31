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


}
