package com.internet.shop.exception;

public abstract class NotFoundException extends RuntimeException{

    public NotFoundException(String message) {
        super(message);
    }
}
