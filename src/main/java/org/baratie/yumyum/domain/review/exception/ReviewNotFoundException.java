package org.baratie.yumyum.domain.review.exception;

import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.exception.GlobalException;

public class ReviewNotFoundException extends GlobalException {
    public ReviewNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
