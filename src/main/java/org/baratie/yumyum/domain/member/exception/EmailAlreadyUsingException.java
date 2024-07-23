package org.baratie.yumyum.domain.member.exception;

import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.exception.GlobalException;

public class EmailAlreadyUsingException extends GlobalException {

    public EmailAlreadyUsingException(ErrorCode errorCode) {
        super(errorCode);
    }
}

