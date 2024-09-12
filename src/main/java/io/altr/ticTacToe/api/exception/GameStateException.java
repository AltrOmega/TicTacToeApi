package io.altr.ticTacToe.api.exception;

public class GameStateException extends RuntimeException{
    public GameStateException(String message) {
        super(message);
    }

    public GameStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
