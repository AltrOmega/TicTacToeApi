package io.altr.ticTacToe.api.exception;

import io.altr.ticTacToe.engine.Mark;

public class GameStateWonException extends GameStateException{

    public GameStateWonException(String message) {
        super(message);
    }
}
