package com.todow.e.exceptions;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class MalformedInputException extends Exception {
    public MalformedInputException(String message) {
        super(message);
    }
}
