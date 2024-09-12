package io.altr.ticTacToe.api.exception;

import io.altr.ticTacToe.engine.Mark;

public class GameStateOutOfTurnException extends  GameStateException{

    public GameStateOutOfTurnException(String message) {
        super(message);
    }
}
