package com.jb.service.ex;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(String massage) {
        super(massage);
    }
}
