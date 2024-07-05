package org.baratie.yumyum.domain.member.exception;

import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.exception.GlobalException;

public class PasswordNotEqualException extends GlobalException {
    public PasswordNotEqualException(ErrorCode errorCode) {
        super(errorCode);
    }
}
