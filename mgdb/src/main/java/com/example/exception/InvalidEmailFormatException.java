package com.example.exception;

public class InvalidEmailFormatException extends Exception {
    public InvalidEmailFormatException() {
        super("The email provided is not in a valid format.");
    }
}
