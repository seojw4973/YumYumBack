package org.baratie.yumyum.domain.store.exception;

import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.exception.GlobalException;

public class StoreNotFoundException extends GlobalException {
    public StoreNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
