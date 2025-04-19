package com.example.exception;

public class CarNotAvailableException extends Exception {
    public CarNotAvailableException() {
        super("Selected car is not available for sale.");
    }
}
