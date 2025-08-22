package com.example.global.exception;

public class ConflictException extends RuntimeException {
    private final String code;
    public ConflictException(String code, String message) { super(message); this.code = code; }
    public String getCode() { return code; }
}
