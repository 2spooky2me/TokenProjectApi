package com.jb.service.ex;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String massage) {
        super(massage);
    }
}
