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
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
    MEMBER_NOT_EQUAL(HttpStatus.FORBIDDEN, "작성자만 수정 또는 삭제할 수 있습니다."),
    PASSWORD_NOT_EQUAL(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
    EXIST_MEMBER_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다."),
    FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "이미지 업로드에 실패하였습니다."),
    MEMBER_IS_DELETED(HttpStatus.BAD_REQUEST, "탈퇴한 회원입니다.");

    private final HttpStatus status;
    private final String message;
}
