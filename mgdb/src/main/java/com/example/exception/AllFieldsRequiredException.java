package com.example.exception;

public class AllFieldsRequiredException extends Exception {
    public AllFieldsRequiredException() {
        super("All fields must be filled out.");
    }
}
