package io.altr.ticTacToe.api.exception;

public class GameOfIdNotFoundException extends GameNotFoundException{
    public GameOfIdNotFoundException(String message) {
        super(message);
    }
}
