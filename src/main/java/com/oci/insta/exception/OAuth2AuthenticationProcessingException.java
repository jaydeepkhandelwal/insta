package com.oci.insta.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {

    public OAuth2AuthenticationProcessingException(String msg, Throwable t) {
        super(msg,t);
        this.setStackTrace(t.getStackTrace());
    }

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}
