package com.jb.service.ex;

public class UnauthorizedCouponAccessException extends Throwable {
    public UnauthorizedCouponAccessException(String massage) {
        super(massage);
    }
}
