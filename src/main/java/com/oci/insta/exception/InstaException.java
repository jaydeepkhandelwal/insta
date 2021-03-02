package com.oci.insta.exception;

import lombok.Getter;
import lombok.ToString;

@ToString
public class InstaException extends Exception {
    private static final long serialVersionUID = 1L;
    @Getter
    private InstaErrorCode code;

    public InstaException(InstaErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public InstaException(InstaErrorCode code, String message, Throwable e) {
        super(message);
        this.setStackTrace(e.getStackTrace());
        this.code = code;
    }

    public InstaException(InstaErrorCode code, Throwable e) {
        super(e.getMessage());
        this.setStackTrace(e.getStackTrace());
        this.code = code;
    }
}
