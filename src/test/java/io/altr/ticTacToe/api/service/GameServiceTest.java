
package io.altr.ticTacToe.api.service;

import io.altr.ticTacToe.api.entity.GameEntity;
import io.altr.ticTacToe.api.repository.GameRepository;
import io.altr.ticTacToe.api.service.impl.GameServiceImpl;
import io.altr.ticTacToe.engine.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GameServiceTest {
    // Todo: Finish tests here:
    //       - placeInGame
    //       - add error tests
    // Todo: Give more descriptive names to the tests

    @Mock
    private GameRepository gameRepositoryMock;

    private GameService gameService;
    private AutoCloseable autoCloseable;
    private Game game;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        gameService = new GameServiceImpl(gameRepositoryMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testCreateNewGame() {
        GameEntity ge = new GameEntity();
        when(gameRepositoryMock.save(ge)).thenReturn(ge);

        gameService.createNewGame();

        verify(gameRepositoryMock).save(ge);
    }

    @Test
    void restartGame() {
        GameEntity geToReset = new GameEntity(1234);
        GameEntity ge = new GameEntity();
        Game game = new Game();

        ReflectionTestUtils.setField(ge, "gameId", 1);
        when(gameRepositoryMock.findById(1)).thenReturn(Optional.of(ge));

        ReflectionTestUtils.setField(geToReset, "gameId", 1);
        when(gameRepositoryMock.save(ge)).thenReturn(ge);

        assertThat(gameService.restartGame(1)).isEqualTo(game);
    }

    @Test
    void getGame() {
        Game game = new Game();
        GameEntity ge = new GameEntity();

        ReflectionTestUtils.setField(ge, "gameId", 1);
        when(gameRepositoryMock.findById(1)).thenReturn(Optional.of(ge));

        assertThat(gameService.getGame(1)).isEqualTo(game);
    }

    @Test
    void deleteGame() {
        GameEntity ge = new GameEntity();

        ReflectionTestUtils.setField(ge, "gameId", 1);
        when(gameRepositoryMock.findById(1)).thenReturn(Optional.of(ge));

        assertThat(gameService.deleteGame(1)).isEqualTo(ge);

        verify(gameRepositoryMock).deleteById(1);
    }

    @Test
        // Final boss: leaving for later
    void placeInGame() {
    }
}
