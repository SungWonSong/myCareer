package com.bs.mycareer.Career.entity;


import com.bs.mycareer.Career.dto.CareerDto;
import com.bs.mycareer.User.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User author;


    //실제 db에서 삭제하는 것이 아니라 보이지만 않게끔 하려고 씀! => 실제 db에서
    @Column
    private boolean available;

    public Career(CareerDto careerDto, User user) {
        this.title = careerDto.getTitle();
        this.content = careerDto.getContent();
        this.author = user;
        this.available = true;

    }

    public Career(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.available = true;
    }

    public void edit(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void delete() {
        this.available = false;
    }


    public void setUser(User user) {
    }
}
