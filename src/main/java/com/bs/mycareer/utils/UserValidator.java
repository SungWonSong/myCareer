package com.bs.mycareer.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

@Getter
@Setter
public class UserValidator{
    private static final String EMAIL_PATTERN = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
    // 1. 이메일 표현식
    // ^[0-9a-zA-Z]: 이메일의 시작은 숫자(0-9) 또는 알파벳 대소문자(a-zA-Z) 이어야 합니다.
    // ([-_.]?[0-9a-zA-Z])*: 그 다음에는 하이픈(-), 언더스코어(_), 점(.) 중 하나 또는 없을 수 있고, 그 뒤에는 숫자 또는 알파벳 대소문자가 옵니다. 이 패턴이 0번 이상 반복될 수 있습니다.
    // @: 반드시 @ 기호가 있어야 합니다.
    // [0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*: @ 기호 뒤에도 앞서 설명한 패턴이 반복됩니다.
    // .: 반드시 점(.) 기호가 있어야 합니다.
    // [a-zA-Z]{2,3}$: 이메일의 끝은 알파벳 2개 이상 3개 이하로 끝나야 합니다.
    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W).{7,12}$";
    // 2. 비밀번호 표현식
    // (?=.*[0-9]): 비밀번호가 최소한 하나의 숫자를 포함해야 함
    // (?=.*[a-zA-Z]): 비밀번호가 최소한 하나의 알파벳 문자를 포함해야 함
    // (?=.*\\W): 비밀번호가 최소한 하나의 특수 문자를 포함해야 함
    // .{7,12}: 비밀번호의 길이는 7자 이상, 12자 이하여야 함

    public static boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }
}
