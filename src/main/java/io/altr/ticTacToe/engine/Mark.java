package io.altr.ticTacToe.engine;

import java.util.NoSuchElementException;

public enum Mark {
    NONE(0, '-'),
    X(1, 'X'),
    O(2, 'O');



    final int mask;
    final char symbol;
    Mark(int mask, char symbol){
        this.mask = mask;
        this.symbol = symbol;
    }


    /**
     * Retrieves the `Mark` enum corresponding to the exact given mask value.
     *
     * @param mask The exact mask value of the desired `Mark` enum.
     *              This must be an exact match with one of the defined masks
     *              in the `Mark` enum.
     * @return The `Mark` enum instance that corresponds to the provided mask value.
     * @throws NoSuchElementException if no `Mark` enum is found with the specified mask value.
     *
     * <br>Example usage:
     * <pre>
     *     Mark mark = Mark.fromMask(1); // returns Mark.X <br>
     *     Mark mark = Mark.fromMask(2); // returns Mark.O <br>
     *     Mark mark = Mark.fromMask(0); // returns Mark.NONE <br>
     * </pre>
     */
    static Mark fromMask(int mask){
        for (Mark mark : values()) {
            if (mark.mask == mask) return mark;
        }
        throw new NoSuchElementException("No mark with the mask: \"" + mask + "\" exists.");
    }



    /**
     * Extracts the `Mark` enum based on a bitwise mask extraction at a specific position.
     *
     * The mask is expected to contain multiple 2-bit segments, each representing
     * a possible `Mark` enum value. This method extracts the relevant 2-bit segment
     * from the given mask, starting from the rightmost side (least significant bits)
     * at the position specified by `i`.
     *
     * @param mask The bitwise mask containing the encoded `Mark` values.
     *             This mask may encode multiple `Mark` enums in different positions,
     *             with each `Mark` being represented by 2 bits.
     * @param i The position of the 2-bit segment to extract, starting from the rightmost side.
     *          For example, if `i = 0`, this method will extract the rightmost 2 bits.
     * @return The `Mark` enum that corresponds to the extracted 2-bit value.
     *
     * <br>Example usage:
     * <pre>
     *     int mask = 0b1001; // binary representation <br>
     *     Mark mark = Mark.fromMask(mask, 0); // returns Mark.X (01) <br>
     *     Mark mark = Mark.fromMask(mask, 1); // returns Mark.O (10) <br>
     * </pre>
     */
    public static Mark fromMask(int mask, int i){
        return fromMask( (mask >> i*2) & 0b11 );
    }
}