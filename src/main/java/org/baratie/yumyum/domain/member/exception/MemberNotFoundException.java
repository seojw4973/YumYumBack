package org.baratie.yumyum.domain.member.exception;

import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.exception.GlobalException;

public class MemberNotFoundException extends GlobalException {
    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
