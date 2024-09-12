package io.altr.ticTacToe.engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Optional;

public class Game {
    @JsonIgnore
    private int tileMask = 0b00_00_00__00_00_00__00_00_00;
    private Mark whoWon = Mark.NONE;
    private Mark turnOf = Mark.X;

    static int[] winningSmasks = {
            0b111_000_000,
            0b000_111_000,
            0b000_000_111,
            0b100_100_100,
            0b010_010_010,
            0b001_001_001,
            0b100_010_001,
            0b001_010_100
    };





    public Game(){}

    public Game(int tileMask){
        setTileMask(tileMask);
        int xNum = 0;
        int oNum = 0;
        int mask = 0;
        for(int i=0; i<9; i++){
            mask = ((tileMask >> i*2) & 0b11);
            if(mask == Mark.X.mask) xNum++;
            else if(mask == Mark.O.mask) oNum++;
        }

        this.turnOf = xNum == oNum ? Mark.X : Mark.O;

        if ( hasWon(Mark.X) ) whoWon = Mark.X;
        else if ( hasWon(Mark.O) ) whoWon = Mark.O;
    }





    public Mark getWhoWon() {
        return whoWon;
    }

    public Mark getTurnOf() {
        return turnOf;
    }

    public int getTileMask(){
        return tileMask;
    }
    void clearTiles(){
        setTileMask(0);
    }

    void setTileMask(int tileMask) {
        this.tileMask = tileMask;
    }

    void setTiles(int smask, Mark mark){
        clearTiles();
        paintTiles(smask, mark);
    }

    void paintTiles(int tiles){
        this.tileMask |= tiles;
    }

    void paintTiles(int smask, Mark mark){
        for (int i=8; i>=0; i--){
            if ((smask >> i & 0b1) == 0b0) continue;
            paintTiles(mark.mask << i*2);
        }
    }





    /**
     * Checks if the current game board's tile states match the specified mask for the given mark.
     *
     * @param smask A 9-bit mask where each bit represents whether a tile should be checked
     *                (1 means check, 0 means ignore). The mask is compared against the current
     *                state of the game board.
     * @param mark The `Mark` (X, O, or NONE) that the tiles in the mask should have. The method
     *               checks if all relevant tiles hold this mark.
     * @return `true` if the tiles on the board corresponding to the `smask` hold the given `mark`,
     *         `false` otherwise.
     *
     * <br>Example usage:
     * <pre>
     *     int mask = 0b111_000_000; // Top row <br>
     *     boolean result = game.isState(mask, Mark.X); // checks if top row is all X <br>
     * </pre>
     */
    boolean isState(int smask, Mark mark){
        for (int i=8; i>=0; i--){
            if ((smask >> i & 0b1) == 0b0) continue;
            if (Mark.fromMask(tileMask, i) == mark) continue;
            return false;
        }
        return true;
    }





    /**
     * Places the specified mark on the game board at the specified position if all conditions are met.
     *
     * This method handles the logic of placing a mark on the board, ensuring that the move is valid
     * according to the rules of the game. It checks if the game has already been won, if the move is
     * being made out of turn, and if the selected tile is already occupied.
     *
     * @param mark - The `Mark` to be placed (X or O).
     * @param pos - The position on the board (0-8) where the mark should be placed.
     * @return An `Optional<Misplace>` indicating if the move was invalid. If the move is valid,
     *         the Optional will be empty. If invalid, it will contain a `Misplace` enum value
     *         describing the reason (e.g., GAME_ENDED, TILE_TAKEN, etc.).
     *
     * <br>Example usage:
     * <pre>
     *     Optional<Misplace> result = game.doPlace(Mark.X, 4); // Tries to place an X in the center <br>
     *     if (result.isPresent()) { <br>
     *         // Handle invalid move <br>
     *     } <br>
     * </pre>
     */
    Optional<Misplace> doPlace(Mark mark, int pos){
        if (whoWon != Mark.NONE) return Optional.of(Misplace.GAME_ENDED);
        if (mark == Mark.NONE) return Optional.of(Misplace.NONE_PLACE);
        if (mark != turnOf) return Optional.of(Misplace.OUT_OF_TURN);
        if ( ((tileMask >> pos*2) & 0b11) != 0b00)
            return Optional.of(Misplace.TILE_TAKEN);
        tileMask |= mark.mask << pos*2;

        if(turnOf == Mark.X) turnOf = Mark.O;
        else turnOf = Mark.X;

        if (hasWon(mark)) whoWon = mark;
        return Optional.empty();
    }

    /**
     * Places the specified mark on the game board at the specified position using a `Pos` enum.
     *
     * This is an overloaded version of `doPlace` that takes a `Pos` enum instead of an integer
     * position. It allows for more readable code by using named positions (e.g., `Pos.b2` instead
     * of `4`).
     *
     * @param mark - The `Mark` to be placed (X or O).
     * @param pos - The `Pos` enum representing the position on the board (e.g., `Pos.a3` for the top-left corner).
     * @return An `Optional<Misplace>` indicating if the move was invalid. If the move is valid,
     *         the Optional will be empty. If invalid, it will contain a `Misplace` enum value
     *         describing the reason.
     *
     * <br>Example usage:
     * <pre>
     *     Optional<Misplace> result = game.doPlace(Mark.X, Pos.b2); // Tries to place an X in the center <br>
     *     if (result.isPresent()) { <br>
     *         // Handle invalid move <br>
     *     } <br>
     * </pre>
     */
    public Optional<Misplace> doPlace(Mark mark, Pos pos){
        return doPlace(mark, pos.val);
    }





    /**
     * Retrieves the current state of the game board as an array of `Mark` values.
     *
     * @return An array of `Mark` values representing the current state of the game board.
     *         The array is indexed from 0 to 8, corresponding to positions on the board:
     *         <pre>
     *         <br>8 | 7 | 6
     *         <br>---------
     *         <br>5 | 4 | 3
     *         <br>---------
     *         <br>2 | 1 | 0
     *         </pre>
     */
    public Mark[] getBoard() {
        Mark[] ret = new Mark[9];
        for (int i=0; i<9; i++) {
            ret[i] = Mark.fromMask(tileMask, 8-i);
        }
        return ret;
    }



    /**
     * Creates an array of `Mark` values, based on smask, and mark provided.
     *
     * @param smask A 9-bit mask where each bit represents whether a tile should be filled with a given Mark when
     *         creating a list of marks (1 means fill with mark, 0 means fill with Mark.NONE).
     *         state of the game board.
     * @param mark The `Mark` (X, O, or NONE) that the array should be filled with.
     * @return An array of `Mark` values representing the given smask.
     *         The array is indexed from 0 to 8, corresponding to positions on the board:
     *         <pre>
     *         <br>8 | 7 | 6
     *         <br>---------
     *         <br>5 | 4 | 3
     *         <br>---------
     *         <br>2 | 1 | 0
     *         </pre>
     */
    static Mark[] makeMarkArr(int smask, Mark mark) {
        Mark[] ret = new Mark[9];
        for (int i=0; i<9; i++) {
            ret[i] = (smask >> 8-i & 0b1) == 0b0 ? Mark.NONE : mark;
        }
        return ret;
    }





    /**
     * Checks if the specified mark has won the game by forming a winning combination.
     *
     * A winning combination is defined as having three of the same marks in a row, column, or diagonal.
     *
     * @param mark - The `Mark` to check for a win condition (X or O).
     * @return `true` if the specified mark has formed a winning combination, `false` otherwise.
     *
     * <br>Example usage:
     * <pre>
     *     boolean xHasWon = game.hasWon(Mark.X); // Check if X has won the game <br>
     * </pre>
     */
    public boolean hasWon(Mark mark){
        if (mark == Mark.NONE) return false;

        for (int smask: winningSmasks){
            if (isState(smask, mark)) return true;
        }
        return false;
    }





    /**
     * Provides a string representation of the current game board.
     *
     * The board is represented in a 3x3 grid format, with each mark's symbol displayed
     * in its respective position. This method is useful for quickly visualizing the
     * state of the game, while debugging.
     *
     * @return A string representing the current state of the game board in a 3x3 grid.
     *
     * <br>Example output:
     * <pre>
     *     <br>X - O
     *     <br>- X -
     *     <br>O - X
     * </pre>
     */
    @Override
    public String toString() {
        Mark[] board = getBoard();
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<9; i++) {
            if (i%3 == 0 & i != 0) sb.append('\n');
            sb.append(board[i].symbol).append(" ");
        } return sb.toString();
    }
}
