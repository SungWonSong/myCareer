package com.bs.mycareer.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private final ResponseCode errorCode;

}