package com.bs.mycareer.entity;

import com.bs.mycareer.dto.CareerDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

// domain이라고 하며 도메인 객체(Entity)
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String contents;


    //실제 db에서 삭제하는 것이 아니라 보이지만 않게끔 하려고 씀! => 실제 db에서
    @Column
    private boolean available;

    public Career(CareerDto careerDto, User user) {
        this.title = careerDto.getTitle();
        this.contents = careerDto.getContents();
        this.user = user;

    }

    public Career(String title, String contents, boolean available) {
        this.title = title;
        this.contents = contents;
        this.available = isAvailable();
    }

    public void delete() {
        this.available = false;
    }


}
