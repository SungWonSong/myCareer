package com.bs.mycareer.Career.dto;

import com.bs.mycareer.Career.entity.Career;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareerDto {

//    private Long id; 보안상의 이유로 식별자는 전달하지 않는다.

    private String title;

    private String contents;

    public CareerDto() {}

    //오버로딩 - 생성자 여러개 만들기 가능한거
    //문자열로 직접 제목(title)과 내용(contents)을 전달하여 CareerDto 객체를 생성할때 사용 -> request할때
    public CareerDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    // Career 객체를 받는 생성자 추가 -> response할때
    public CareerDto(Career career) {
        this.title = career.getTitle();
        this.contents = career.getContents();
    }
}