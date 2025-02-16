package com.jb.service.ex;

public class NoSuchCompanyException extends RuntimeException {
    public NoSuchCompanyException(String message) {
        super(message);
    }
}
