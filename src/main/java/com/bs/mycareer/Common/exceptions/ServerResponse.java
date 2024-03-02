package com.bs.mycareer.Common.exceptions;


import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ServerResponse {
    private final int status;
    private final String message;

    public static ResponseEntity<ServerResponse> toResponseEntity(ResponseCode responseCode) {
        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .body(ServerResponse.builder()
                        .status(responseCode.getHttpStatus().value())
                        .message(responseCode.getDetail())
                        .build()
                );
    }
}
