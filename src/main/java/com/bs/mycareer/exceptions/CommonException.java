package com.bs.mycareer.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CommonException extends RuntimeException{

    public CommonException(String msg){
        super(msg);
    }
    public CommonException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public CommonException(Throwable cause) {
        super(cause);
    }
}
