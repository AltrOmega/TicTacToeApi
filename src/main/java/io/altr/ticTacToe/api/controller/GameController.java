package io.altr.ticTacToe.api.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.altr.ticTacToe.api.entity.GameEntity;
import io.altr.ticTacToe.api.service.GameService;
import io.altr.ticTacToe.engine.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GameController {

    private final Logger logInfo = LoggerFactory.getLogger(GameController.class);
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }





    /**
     * Creates a new Tic-Tac-Toe game and returns its gameId.
     * This endpoint consumes no JSON and returns a response with the gameId of the newly created game.
     * <br>Sample response JSON:
     * <pre>
     * {
     *     "gameId": 1
     * }
     * </pre>
     *
     * @return ResponseEntity containing the GameEntity with the gameId of the newly created game, or an error.
     */
    @PostMapping("/newGame")
    public ResponseEntity<GameEntity> createGame() {
        GameEntity ge = gameService.createNewGame();
        logInfo.info("New Game created with gameId: {}", ge.getGameId());
        return ResponseEntity.ok(ge);
    }





    /**
     * Restarts the game with the specified gameId and returns the new game state in JSON format.
     * This endpoint consumes no JSON.
     * <br>Sample response JSON:
     * <pre>
     * {
     * "whoWon": "NONE",
     * "turnOf": "X",
     * "board": [
     *     "NONE",
     *     "NONE",
     *     "NONE",
     *     "NONE",
     *     "NONE",
     *     "NONE",
     *     "NONE",
     *     "NONE",
     *     "NONE"
     *  ]
     * }
     * </pre>
     *
     * @param gameId the ID of the game to restart
     * @return ResponseEntity containing the restarted game, or an error.
     */
    @PutMapping("/{gameId}/restart")
    public ResponseEntity<Game> restartGame(@PathVariable Integer gameId) {
        logInfo.info("Restart game requested for gameId: {}", gameId);
        return ResponseEntity.ok(gameService.restartGame(gameId));
    }





    /**
     * Retrieves the current state of the game with the given gameId in JSON format.
     *
     * @param gameId the ID of the game to retrieve
     * @return ResponseEntity containing the game state, or an error.
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable Integer gameId) {
        logInfo.info("Get game requested for gameId: {}", gameId);
        return ResponseEntity.ok(gameService.getGame(gameId));
    }





    /**
     * Deletes the game with the specified gameId and returns the deleted game entity.
     *
     * @param gameId the ID of the game to delete
     * @return ResponseEntity containing the deleted GameEntity, or an error.
     */
    @DeleteMapping("/{gameId}/delete")
    public ResponseEntity<GameEntity> deleteGame(@PathVariable Integer gameId) {
        logInfo.info("Delete game requested for gameId: {}", gameId);
        return ResponseEntity.ok(gameService.deleteGame(gameId));
    }





    /**
     * Places a mark (X or O) at a specified position on the game board.
     * The JSON request body must contain the mark and position fields.
     * <br>Sample request JSON:
     * <pre>
     * {
     *     "mark": "X",
     *     "pos": "a1"
     * }
     * </pre>
     * <br>Sample response JSON (after mark placement):
     * <pre>
     * {
     * "whoWon": "NONE",
     * "turnOf": "O",
     * "board": [
     *     "X",
     *     "NONE",
     *     "NONE",
     *     "NONE",
     *     "NONE",
     *     "NONE",
     *     "NONE",
     *     "NONE",
     *     "NONE"
     *  ]
     * }
     * </pre>
     *
     * @param gameId the ID of the game where the mark is to be placed
     * @param objectNode JSON object containing the mark and position fields
     * @return ResponseEntity containing the updated game state after the mark is placed, or an error.
     */
    @PutMapping("/{gameId}/place")
    public ResponseEntity<Game> doPlace(@PathVariable Integer gameId, @RequestBody ObjectNode objectNode) {
        logInfo.info("Place requested for gameId: {}", gameId);
        return ResponseEntity.ok(gameService.placeInGame(gameId, objectNode));
    }
}
