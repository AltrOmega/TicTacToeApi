package io.altr.ticTacToe.api.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.altr.ticTacToe.api.entity.GameEntity;
import io.altr.ticTacToe.api.exception.*;
import io.altr.ticTacToe.api.repository.GameRepository;
import io.altr.ticTacToe.api.service.GameService;
import io.altr.ticTacToe.engine.Game;
import io.altr.ticTacToe.engine.Mark;
import io.altr.ticTacToe.engine.Misplace;
import io.altr.ticTacToe.engine.Pos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final Logger logInfo = LoggerFactory.getLogger(GameServiceImpl.class);
    private final GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }





    /**
     * Creates a new game and saves it to the repository.
     *
     * @return a new GameEntity instance representing the created game
     */
    public GameEntity createNewGame() {
        GameEntity ge = new GameEntity();
        gameRepository.save(ge);
        logInfo.info("New Game created with gameId: {}", ge.getGameId());
        return ge;
    }





    /**
     * Restarts a game by creating a new game instance and overwriting the existing one.
     *
     * @param gameId the ID of the game to restart
     * @return the newly restarted game instance
     * @throws GameOfIdNotFoundException if no game with the given ID is found
     */
    public Game restartGame(Integer gameId) {
        Optional<GameEntity> gameEnOp = gameRepository.findById(gameId);
        if (gameEnOp.isEmpty()) {
            logInfo.info("Failed to restart game with gameId: {}", gameId);
            throw new GameOfIdNotFoundException("No game with gameId: \"" + gameId + "\" was found.");
        }
        logInfo.info("Successfully restarted game with gameId: {}", gameId);
        return overwriteGame(gameId, new Game());
    }





    /**
     * Retrieves a game by its ID.
     *
     * @param gameId the ID of the game to retrieve
     * @return the game instance associated with the provided ID
     * @throws GameOfIdNotFoundException if no game with the given ID is found
     */
    public Game getGame(Integer gameId) {
        Optional<GameEntity> gameEnOp = gameRepository.findById(gameId);
        if (gameEnOp.isEmpty()) {
            logInfo.info("Failed to retrieve game with gameId: {}", gameId);
            throw new GameOfIdNotFoundException("No game with gameId: \"" + gameId + "\" was found.");
        }

        logInfo.info("Successfully retrieved game with gameId: {}", gameId);
        return convertToDomain(gameEnOp.get());
    }





    /**
     * Deletes a game from the repository by its ID.
     *
     * @param gameId the ID of the game to delete
     * @return the deleted GameEntity instance
     * @throws GameOfIdNotFoundException if no game with the given ID is found
     */
    public GameEntity deleteGame(Integer gameId) {
        Optional<GameEntity> gameEnOp = gameRepository.findById(gameId);
        if (gameEnOp.isEmpty()) {
            logInfo.info("Failed to delete game with gameId: {}", gameId);
            throw new GameOfIdNotFoundException("No game with gameId: \"" + gameId + "\" was found.");
        }

        logInfo.info("Successfully deleted game with gameId: {}", gameId);
        gameRepository.deleteById(gameId);
        return gameEnOp.get();
    }





    /**
     * Places a mark (X or O) at a given position in the game.
     *
     * @param gameId the ID of the game in which to place the mark
     * @param objectNode JSON object containing the mark and position data
     * @return the updated game instance after placing the mark
     * @throws GameOfIdNotFoundException if no game with the given ID is found
     * @throws MissingFieldOfNameException if the "mark" or "pos" fields are missing in the JSON object
     * @throws WrongFieldNameException if the values for "mark" or "pos" are invalid
     * @throws GameStateWonException if the game has already ended
     * @throws GameStateOutOfTurnException if it is not the correct turn for the mark being placed
     */
    public Game placeInGame(Integer gameId, ObjectNode objectNode) {
        Optional<GameEntity> gameEnOp = gameRepository.findById(gameId);
        if (gameEnOp.isEmpty()) {
            logPlaceFail(gameId);
            throw new GameOfIdNotFoundException("No game with gameId: \"" + gameId + "\" was found.");
        }

        Game game = convertToDomain(gameEnOp.get());

        Mark mark;
        Pos pos;
        try {
            mark = Mark.valueOf(objectNode.get("mark").asText());
        } catch (NullPointerException e) {
            logPlaceFail(gameId);
            throw new MissingFieldOfNameException("Given JSON has no \"mark\" field.");
        } catch (IllegalArgumentException e) {
            logPlaceFail(gameId);
            throw new WrongFieldNameException("Json field: \"mark\" is of wrong value. Should be: \"X\" or \"O\".");
        }

        try {
            pos = Pos.valueOf(objectNode.get("pos").asText());
        } catch (NullPointerException e) {
            logPlaceFail(gameId);
            throw new MissingFieldOfNameException("Given JSON has no \"pos\" field.");
        } catch (IllegalArgumentException e) {
            logPlaceFail(gameId);
            throw new WrongFieldNameException("Json field: \"pos\" is of wrong value.");
        }

        Optional<Misplace> misplaceOp = game.doPlace(mark, pos);
        if (misplaceOp.isPresent()) {
            Misplace misplace = misplaceOp.get();
            if (misplace == Misplace.NONE_PLACE) {
                logPlaceFail(gameId);
                throw new WrongFieldNameException("Json field: \"mark\" is of \"NONE\".");
            }
            if (misplace == Misplace.GAME_ENDED) {
                logPlaceFail(gameId);
                throw new GameStateWonException("Game has already ended. Mark \"" + game.getWhoWon() + "\" has won.");
            }
            if (misplace == Misplace.OUT_OF_TURN) {
                logPlaceFail(gameId);
                throw new GameStateOutOfTurnException("Can't place the mark of: \"" + mark + "\" now. It's not your turn.");
            }
            if (misplaceOp.get() == Misplace.TILE_TAKEN) {
                logPlaceFail(gameId);
                throw new WrongFieldNameException("Pos: \"" + pos + "\" is already taken.");
            }
        }

        logInfo.info("Successfully placed mark in game with gameId: {}", gameId);
        return overwriteGame(gameId, game);
    }

    private void logPlaceFail(Integer gameId) {
        logInfo.info("Failed to place in game with gameId: {}", gameId);
    }





    private Game overwriteGame(Integer gameId, Game game) {
        Optional<GameEntity> gameEnOp = gameRepository.findById(gameId);
        if (gameEnOp.isEmpty()) {
            throw new GameOfIdNotFoundException("No game with gameId: \"" + gameId + "\" was found.");
        }
        gameRepository.save(convertToEntity(gameId, game));
        return getGame(gameId);
    }





    private GameEntity convertToEntity(Game game) {
        return new GameEntity(game.getTileMask());
    }

    private GameEntity convertToEntity(Integer gameId, Game game) {
        return new GameEntity(gameId, game.getTileMask());
    }

    private Game convertToDomain(GameEntity gameEntity) {
        return new Game(gameEntity.getTileMask());
    }
}
