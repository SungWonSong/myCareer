package com.bs.mycareer.Career.entity;

import com.bs.mycareer.User.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// domain이라고 하며 도메인 객체(Entity)
@Entity
@Getter
@Setter
@NoArgsConstructor

public class Career {

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id값 auto 자동할당
    private Long id;

    private String title;

//    @Column(columnDefinition = "TEXT")
    private String contents;

}
