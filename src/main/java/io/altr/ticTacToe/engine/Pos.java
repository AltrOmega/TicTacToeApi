package io.altr.ticTacToe.engine;

public enum Pos {
    a3(8), b3(7), c3(6),
    a2(5), b2(4), c2(3),
    a1(2), b1(1), c1(0);

    final int val;
    Pos(int val){
        this.val = val;
    }
}
