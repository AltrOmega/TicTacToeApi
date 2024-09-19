package io.altr.ticTacToe.api.controller;

import io.altr.ticTacToe.api.entity.GameEntity;
import io.altr.ticTacToe.api.service.GameService;
import io.altr.ticTacToe.engine.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {
    // Todo: Fix h2 driver config
    // Todo: Finish tests here, give them more descriptive names

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GameService gameServiceMock = mock(GameService.class);
    private Game game_1;
    private GameEntity ge_1;
    private Game game_2;
    private GameEntity ge_2;

    @BeforeEach
    void setUp() {
        game_1 = new Game();
        game_2 = new Game();
        ge_1 = new GameEntity();
        ge_2 = new GameEntity();
        ReflectionTestUtils.setField(ge_1, "gameId", 1);
        ReflectionTestUtils.setField(ge_2, "gameId", 2);
    }





    @Test
    void testCreateGame() throws Exception{
        GameEntity ge = new GameEntity();
        when(gameServiceMock.createNewGame()).thenReturn(ge);

        gameServiceMock.createNewGame();

        this.mockMvc.perform(post("/api/newGame")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void restartGame() {
    }

    @Test
    void getGame() {
    }

    @Test
    void deleteGame() {
    }

    @Test
    void doPlace() {
    }
}