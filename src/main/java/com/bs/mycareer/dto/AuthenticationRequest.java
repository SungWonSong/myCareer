package com.bs.mycareer.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY) // 가시성...
public class AuthenticationRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -1695490485907383846L;

    private String email;
    private String password;
    // refreshToken은 필드로 지정되었다고 입력값이 아니다. 로그인 성공후 발급되어 가지게 되는 필드이다.
    // accessToken은 로그인에 성공하면 자체가 필드이기 때문에 따로 필드값을 입력하지 않아도 됩니다.
    private String refreshToken;

}
