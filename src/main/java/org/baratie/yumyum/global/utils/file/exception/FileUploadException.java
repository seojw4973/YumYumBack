package org.baratie.yumyum.global.utils.file.exception;

import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.exception.GlobalException;

public class FileUploadException extends GlobalException {

    public FileUploadException(ErrorCode errorCode) {
        super(errorCode);
    }
}
