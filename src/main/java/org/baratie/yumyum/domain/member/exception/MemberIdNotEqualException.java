package org.baratie.yumyum.domain.member.exception;

import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.exception.GlobalException;

public class MemberIdNotEqualException extends GlobalException {
    public MemberIdNotEqualException(ErrorCode errorCode) {
        super(errorCode);
    }
}
