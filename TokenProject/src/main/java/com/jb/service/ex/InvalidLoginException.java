package com.jb.service.ex;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String massage) {
        super(massage);
    }
}
