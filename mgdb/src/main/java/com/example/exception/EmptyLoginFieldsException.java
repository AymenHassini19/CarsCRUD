package com.example.exception;

public class EmptyLoginFieldsException extends Exception {
    public EmptyLoginFieldsException() {
        super("Please provide both email and password.");
    }

}
