package com.example.exception;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException() {
        super("Email or password is incorrect.");
    }
}
