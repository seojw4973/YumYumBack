package org.baratie.yumyum.domain.member.exception;

import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.exception.GlobalException;

public class DeletedMemberException extends GlobalException {
    public DeletedMemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
