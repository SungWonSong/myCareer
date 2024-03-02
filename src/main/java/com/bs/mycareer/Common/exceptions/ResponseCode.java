package com.bs.mycareer.Common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


@Getter
@AllArgsConstructor
public enum ResponseCode {

    /* 200 OK : 성공 */

    // 회원가입 성공
    SUCCESS_SIGNUP(OK, "회원가입이 완료되었습니다."),

    // 로그인 성공
    SUCCESS_SIGNIN(OK, "로그인이 완료되었습니다."),

    // 글 작성 성공
    SUCCESS_CREATE(OK, "성공적으로 커리어를 작성하였습니다."),

    // 글 수정 성공
    SUCCESS_EDIT(OK, "커리어 수정이 완료되었습니다."),

    // 글 삭제 성공
    SUCCESS_DELETE(OK, "커리어를 성공적으로 삭제하였습니다."),


    /* 400 BAD_REQUEST : 잘못된 요청 */

    // 회원가입 시, EMAIL, PASSWORD의 요구되는 패턴에 적합하지 않았을 경우
    INVALID_EMAIL_PATTERN(BAD_REQUEST, "이메일 형식이 올바르지 않습니다."),
    INVALID_PASSWORD_PATTERN(BAD_REQUEST, "비밀번호는 8글자 이상, 15글자 이하의 알파벳, 숫자, 특수문자를 각 각 하나 이상 포함하여 구성해야 합니다."),


    // 로그인에 실패하는 경우 (비밀번호가 맞지 않는 경우)
    DUPLICATE_LOGIN(BAD_REQUEST, "중복된 로그인입니다."),
    INVALID_SIGN_IN_EMAIL(BAD_REQUEST, "이메일에 빈 칸을 입력할 수 없습니다."),
    INVALID_SIGN_IN_PASSWORD(BAD_REQUEST, "비밀번호에 빈 칸을 입력할 수 없습니다."),
    INVALID_PASSWORD(BAD_REQUEST, "비밀번호가 맞지 않습니다"),

    // 글 작성 시 제목에 빈 칸을 입력한 경우
    INVALID_CAREER_TITLE(BAD_REQUEST, "제목에 빈 칸을 입력할 수 없습니다."),
    // 글 작성 시 내용에 빈 칸을 입력한 경우
    INVALID_CONTENT(BAD_REQUEST, "내용에 빈 칸을 입력할 수 없습니다."),
    // 글 수정 시 제목, 내용에 모두 빈 칸을 입력한 경우
    INVALID_EDIT_VALUE(BAD_REQUEST, "수정할 내용이 없습니다."),

    // 삭제된 글 다시 삭제 실패
    CAREER_IS_DELETED(NOT_FOUND, "삭제된 글입니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    // 토큰이 있고, 유효한 토큰이지만 해당 사용자가 작성한 게시글/댓글이 아닌 경우
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "삭제/수정할 수 있는 권한이 없습니다."),

    /* 404 NOT_FOUND : 리소스를 찾을 수 없음 */
    // 로그인 시, 회원을 찾을 수 없는 경우
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 이메일을 가진 회원을 찾을 수 없습니다."),
    // 게시글을 찾을 수 없는 경우
    CAREER_NOT_FOUND(NOT_FOUND, "글을 찾을 수 없습니다."),


    // 이미 존재하는 email로 회원가입을 요청한 경우
    DUPLICATE_USER(BAD_REQUEST, "중복된 이메일의 회원입니다."),
    DUPLICATE_RESOURCE(BAD_REQUEST, "중복된 리소스입니다."),


    //서버에러
    INTERNAL_SERVER_ERROR(BAD_REQUEST, "서버에러 입니다."),

    //토큰 에러
    INVALID_ACCESS_TOKEN(BAD_REQUEST, "잘못된 ACCESS 토큰값 입니다."),

    INVALID_TOKEN(BAD_REQUEST, "잘못된 토큰값 입니다."),

    //토큰에서 사용자 정보 검색에 실패했을때 Failed to retrieve user details
    FAILED_TO_SEARCH_USER_DETAILS(BAD_REQUEST, "사용자 정보를 찾을 수 없습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
