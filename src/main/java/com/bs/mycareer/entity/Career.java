package com.bs.mycareer.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

// domain이라고 하며 도메인 객체(Entity)
@Entity
public class Career {

    private String title;

    private String contents;



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
