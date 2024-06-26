package org.baratie.yumyum.domain.store.exception;

import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.exception.GlobalException;

public class StoreExistException extends GlobalException {

    public StoreExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
