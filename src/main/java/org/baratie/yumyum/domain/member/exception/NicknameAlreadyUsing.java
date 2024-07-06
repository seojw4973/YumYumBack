package org.baratie.yumyum.domain.member.exception;

import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.exception.GlobalException;

public class NicknameAlreadyUsing extends GlobalException {
    public NicknameAlreadyUsing(ErrorCode errorCode) {
        super(errorCode);
    }
}
