package com.example.UserService.exception;

public class UnAuthorizeException extends RuntimeException{
    public UnAuthorizeException(String message) {
        super(message);
    }
}
