package io.altr.ticTacToe.api.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.altr.ticTacToe.api.entity.GameEntity;
import io.altr.ticTacToe.engine.Game;

public interface GameService {
    public GameEntity createNewGame();
    public Game restartGame(Integer gameId);
    public Game getGame(Integer gameId);
    public GameEntity deleteGame(Integer gameId);
    public Game placeInGame(Integer gameId, ObjectNode objectNode);
}
