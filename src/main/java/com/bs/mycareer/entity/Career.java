package com.bs.mycareer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @GeneratedValue(strategy = GenerationType.AUTO) // id값 auto 자동할당
    private Long id;

    private String title;

    private String contents;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
