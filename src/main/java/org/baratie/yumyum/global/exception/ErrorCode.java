package org.baratie.yumyum.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."),
    EXIST_STORE_NAME(HttpStatus.BAD_REQUEST, "이미 존재하는 가게입니다.");

    private final HttpStatus status;
    private final String message;
}
