package org.baratie.yumyum.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 가게이거나 폐업한 가게입니다."),
    EXIST_STORE_NAME(HttpStatus.BAD_REQUEST, "이미 존재하는 가게입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."),
    REPLY_NOT_FOUNT(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다.");

    private final HttpStatus status;
    private final String message;
}
