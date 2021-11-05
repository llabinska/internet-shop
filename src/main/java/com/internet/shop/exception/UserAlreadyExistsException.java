package com.internet.shop.exception;

public class UserAlreadyExistsException extends AlreadyExistsException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
