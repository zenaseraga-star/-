package main.java.ru.itmo.ArsikAndEva.exception;


public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}