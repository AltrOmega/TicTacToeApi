package io.altr.ticTacToe.engine;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {


    @Test
    void gameFromTiles(){
        Game game = new Game(0b01_01_01__00_00_00__00_10_10);
        assertEquals(Mark.O, game.getTurnOf());
        assertEquals(Mark.X, game.getWhoWon());

        game = new Game(0b01_01_00__00_00_00__00_10_10);
        assertEquals(Mark.X, game.getTurnOf());
        assertEquals(Mark.NONE, game.getWhoWon());

        game = new Game(0b01_01_10__01_00_00__00_10_10);
        assertEquals(Mark.X, game.getTurnOf());
        assertEquals(Mark.NONE, game.getWhoWon());

        game = new Game(0b01_01_10__01_01_00__00_10_10);
        assertEquals(Mark.O, game.getTurnOf());
        assertEquals(Mark.NONE, game.getWhoWon());

        game = new Game(0b01_01_10__01_01_00__10_10_10);
        assertEquals(Mark.X, game.getTurnOf());
        assertEquals(Mark.O, game.getWhoWon());
    }





    void isStateHelper(Mark mark){
        Game game = new Game();
        switch (mark){
            case NONE -> game.setTileMask(0b01_01_01__01_01_01__01_00_00);
            case X -> game.setTileMask(0b00_00_00__00_00_00__00_01_01);
            case O -> game.setTileMask(0b00_00_00__00_00_00__00_10_10);
        }
        assertTrue(game.isState(0b000_000_000, mark));
        assertTrue(game.isState(0b000_000_001, mark));
        assertTrue(game.isState(0b000_000_010, mark));
        assertTrue(game.isState(0b000_000_011, mark));
        assertFalse(game.isState(0b111_111_111, mark));
        assertFalse(game.isState(0b100_000_011, mark));
        assertFalse(game.isState(0b100_000_001, mark));
    }



    @Test
    void isState_NONE(){
        isStateHelper(Mark.NONE);
    }



    @Test
    void isState_X(){
        isStateHelper(Mark.X);
    }



    @Test
    void isState_O(){
        isStateHelper(Mark.O);
    }





    @Test
    void doPlace_GameEndedCheckTest(){
        Game game = new Game();
        assertTrue(game.doPlace(Mark.X, Pos.a1).isEmpty());
        assertTrue(game.doPlace(Mark.O, Pos.c3).isEmpty());
        assertTrue(game.doPlace(Mark.X, Pos.a2).isEmpty());
        assertTrue(game.doPlace(Mark.O, Pos.c2).isEmpty());
        assertTrue(game.doPlace(Mark.X, Pos.a3).isEmpty());
        assertEquals(game.doPlace(Mark.O, Pos.c1), Optional.of(Misplace.GAME_ENDED));
        assertEquals(game.doPlace(Mark.O, Pos.b2), Optional.of(Misplace.GAME_ENDED));
        assertEquals(game.doPlace(Mark.X, Pos.c1), Optional.of(Misplace.GAME_ENDED));
        assertEquals(game.doPlace(Mark.X, Pos.b2), Optional.of(Misplace.GAME_ENDED));
    }



    @Test
    void doPlace_NonePlaceCheckTest(){
        Game game = new Game();
        assertEquals(game.doPlace(Mark.NONE, Pos.a1), Optional.of(Misplace.NONE_PLACE));
    }



    @Test
    void doPlace_OutOfTurnCheckTest(){
        Game game = new Game();

        assertTrue(game.doPlace(Mark.X, Pos.a1).isEmpty());
        assertEquals(game.doPlace(Mark.X, Pos.b2), Optional.of(Misplace.OUT_OF_TURN));

        assertTrue(game.doPlace(Mark.O, Pos.a2).isEmpty());
        assertEquals(game.doPlace(Mark.O, Pos.b3), Optional.of(Misplace.OUT_OF_TURN));
    }



    @Test
    void doPlace_TileTakenCheckTest(){
        Game game = new Game();

        assertTrue(game.doPlace(Mark.X, Pos.a1).isEmpty());
        assertEquals(game.doPlace(Mark.O, Pos.a1), Optional.of(Misplace.TILE_TAKEN));

        assertTrue(game.doPlace(Mark.O, Pos.a2).isEmpty());
        assertEquals(game.doPlace(Mark.X, Pos.a2), Optional.of(Misplace.TILE_TAKEN));

        assertTrue(game.doPlace(Mark.X, Pos.a3).isEmpty());
        assertEquals(game.doPlace(Mark.O, Pos.a3), Optional.of(Misplace.TILE_TAKEN));

        assertTrue(game.doPlace(Mark.O, Pos.b1).isEmpty());
        assertEquals(game.doPlace(Mark.X, Pos.b1), Optional.of(Misplace.TILE_TAKEN));

        assertTrue(game.doPlace(Mark.X, Pos.b2).isEmpty());
        assertEquals(game.doPlace(Mark.O, Pos.b2), Optional.of(Misplace.TILE_TAKEN));

        assertTrue(game.doPlace(Mark.O, Pos.c3).isEmpty());
        assertEquals(game.doPlace(Mark.X, Pos.c3), Optional.of(Misplace.TILE_TAKEN));
    }





    void getBoardHelper(Mark mark){
        Game game = new Game();

        for(int smask : Game.winningSmasks){
            game.setTiles(smask, mark);
            Mark[] gameBoard = game.getBoard();
            Mark[] markArrBoard = Game.makeMarkArr(smask, mark);

            try{ assertArrayEquals(markArrBoard, gameBoard); }
            catch (AssertionError e){
                System.out.println(
                    "Maks Board:\n" + Arrays.stream(markArrBoard).toList() +
                    "\nGame Board:\n" + Arrays.stream(gameBoard).toList() +
                    "\nSMask:\n" + Integer.toBinaryString(smask)
                );
                throw e;
            }
        }
    }



    @Test
    void getBoard_NONE() {
        Game game = new Game();
        assertArrayEquals(Game.makeMarkArr(0, Mark.NONE), game.getBoard());
    }

    @Test
    void getBoard_X() {
        getBoardHelper(Mark.X);
    }

    @Test
    void getBoard_O() {
        getBoardHelper(Mark.O);
    }





    @Test
    void hasWon_NONE() {
        Game game = new Game();

        game.setTileMask(0b00_00_00__00_00_00__00_00_00);
        assertFalse(game.hasWon(Mark.NONE));
        assertFalse(game.hasWon(Mark.X));
        assertFalse(game.hasWon(Mark.O));
    }



    @Test
    void hasWon_X() {
        Game game = new Game();

        game.setTileMask(0b01_01_01__00_00_00__00_00_00);
        assertTrue(game.hasWon(Mark.X));
        assertFalse(game.hasWon(Mark.O));

        game.setTileMask(0b00_00_00__01_01_01__00_00_00);
        assertTrue(game.hasWon(Mark.X));
        assertFalse(game.hasWon(Mark.O));

        game.setTileMask(0b00_00_00__00_00_00__01_01_01);
        assertTrue(game.hasWon(Mark.X));
        assertFalse(game.hasWon(Mark.O));

        game.setTileMask(0b01_00_00__01_00_00__01_00_00);
        assertTrue(game.hasWon(Mark.X));
        assertFalse(game.hasWon(Mark.O));

        game.setTileMask(0b00_01_00__00_01_00__00_01_00);
        assertTrue(game.hasWon(Mark.X));
        assertFalse(game.hasWon(Mark.O));

        game.setTileMask(0b00_00_01__00_00_01__00_00_01);
        assertTrue(game.hasWon(Mark.X));
        assertFalse(game.hasWon(Mark.O));

        game.setTileMask(0b01_00_00__00_01_00__00_00_01);
        assertTrue(game.hasWon(Mark.X));
        assertFalse(game.hasWon(Mark.O));

        game.setTileMask(0b00_00_01__00_01_00__01_00_00);
        assertTrue(game.hasWon(Mark.X));
        assertFalse(game.hasWon(Mark.O));
    }



    @Test
    void hasWon_O() {
        Game game = new Game();

        game.setTileMask(0b10_10_10__00_00_00__00_00_00);
        assertFalse(game.hasWon(Mark.X));
        assertTrue(game.hasWon(Mark.O));

        game.setTileMask(0b00_00_00__10_10_10__00_00_00);
        assertFalse(game.hasWon(Mark.X));
        assertTrue(game.hasWon(Mark.O));

        game.setTileMask(0b00_00_00__00_00_00__10_10_10);
        assertFalse(game.hasWon(Mark.X));
        assertTrue(game.hasWon(Mark.O));

        game.setTileMask(0b10_00_00__10_00_00__10_00_00);
        assertFalse(game.hasWon(Mark.X));
        assertTrue(game.hasWon(Mark.O));

        game.setTileMask(0b00_10_00__00_10_00__00_10_00);
        assertFalse(game.hasWon(Mark.X));
        assertTrue(game.hasWon(Mark.O));

        game.setTileMask(0b00_00_10__00_00_10__00_00_10);
        assertFalse(game.hasWon(Mark.X));
        assertTrue(game.hasWon(Mark.O));

        game.setTileMask(0b10_00_00__00_10_00__00_00_10);
        assertFalse(game.hasWon(Mark.X));
        assertTrue(game.hasWon(Mark.O));

        game.setTileMask(0b00_00_10__00_10_00__10_00_00);
        assertFalse(game.hasWon(Mark.X));
        assertTrue(game.hasWon(Mark.O));
    }
}