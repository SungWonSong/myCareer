package com.bs.mycareer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// domain이라고 하며 도메인 객체(Entity)
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id값 auto 자동할당
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String contents;

}
