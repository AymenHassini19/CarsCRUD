package com.example.exception;

public class UnauthorizedDepartmentException extends Exception {
    public UnauthorizedDepartmentException() {
        super("Only users from the management department can log in.");
    }
}
