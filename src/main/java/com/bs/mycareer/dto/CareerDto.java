package com.bs.mycareer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareerDto {

//    private Long id; 보안상의 이유로 식별자는 전달하지 않는다.
    private String title;
    private String contents;
}