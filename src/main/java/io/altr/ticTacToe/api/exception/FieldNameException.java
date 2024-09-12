package io.altr.ticTacToe.api.exception;

public class FieldNameException extends RuntimeException{

    public FieldNameException(String message) {
        super(message);
    }

    public FieldNameException(String message, Throwable cause) {
        super(message, cause);
    }
}