package com.bs.mycareer.Common.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice //스프링 컨트롤러에 대한 전역 예외처리를 정의하는데 사용
public class GlobalExceptionHandler {
    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ServerResponse> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ServerResponse.toResponseEntity(e.getErrorCode());
    }

    //DB데이터 관련 부분 처리
    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<ServerResponse> handleDataException() {
        log.error("handleDataException throw Exception : {}", ResponseCode.DUPLICATE_RESOURCE);
        return ServerResponse.toResponseEntity(ResponseCode.DUPLICATE_RESOURCE);
    }

}
