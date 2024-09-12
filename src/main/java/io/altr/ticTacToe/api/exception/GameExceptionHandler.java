package io.altr.ticTacToe.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GameExceptionHandler {

    @ExceptionHandler(value = {GameNotFoundException.class})
    public ResponseEntity<Object> handleGameNotFoundException(
        GameNotFoundException gameNotFoundException){
        GameException gameException = new GameException(
            gameNotFoundException.getMessage(),
            gameNotFoundException.getCause(),
            HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(gameException, HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler(value = {GameOfIdNotFoundException.class})
    public ResponseEntity<Object> handleGameOfIdNotFoundException(
            GameOfIdNotFoundException gameOfIdNotFoundException){
        GameException gameException = new GameException(
                gameOfIdNotFoundException.getMessage(),
                gameOfIdNotFoundException.getCause(),
                HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(gameException, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(value = {MissingFieldOfNameException.class})
    public ResponseEntity<Object> handleMissingFieldOfNameException(
            MissingFieldOfNameException missingFieldOfNameException){
        GameException gameException = new GameException(
                missingFieldOfNameException.getMessage(),
                missingFieldOfNameException.getCause(),
                HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(gameException, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(value = {WrongFieldNameException.class})
    public ResponseEntity<Object> handleWrongFieldNameException(
            WrongFieldNameException wrongFieldNameException){
        GameException gameException = new GameException(
                wrongFieldNameException.getMessage(),
                wrongFieldNameException.getCause(),
                HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(gameException, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(value = {GameStateWonException.class})
    public ResponseEntity<Object> handleGameStateWonException(
            GameStateWonException gameStateWonException){
        GameException gameException = new GameException(
                gameStateWonException.getMessage(),
                gameStateWonException.getCause(),
                HttpStatus.CONFLICT);

        return new ResponseEntity<>(gameException, HttpStatus.CONFLICT);
    }



    @ExceptionHandler(value = {GameStateOutOfTurnException.class})
    public ResponseEntity<Object> handleGameStateOutOfTurnException(
            GameStateOutOfTurnException gameStateOutOfTurnException){
        GameException gameException = new GameException(
                gameStateOutOfTurnException.getMessage(),
                gameStateOutOfTurnException.getCause(),
                HttpStatus.CONFLICT);

        return new ResponseEntity<>(gameException, HttpStatus.CONFLICT);
    }
}
