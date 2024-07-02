package org.baratie.yumyum.domain.reply.exception;

import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.exception.GlobalException;

public class ReplyNotFoundException extends GlobalException {
    public ReplyNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
