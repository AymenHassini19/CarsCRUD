package com.example.exception;

public class NoItemSelectedException extends Exception {

    public NoItemSelectedException(String itemName) {
        super("No " + itemName + " selected from the table.");
    }
}
