package dev.comhub.test;

import dev.comhub.chessengine.Board;
import dev.comhub.chessengine.Move;
import org.junit.Assert;
import org.junit.Test;

public class TestMain {

    @Test
    public void test_Board_getMove() {
        String s0 = "11111111" +
                "01111111" +
                "00000000" +
                "10000000" +
                "00000000" +
                "00000000" +
                "11111111" +
                "11111111";

        Assert.assertEquals(new Move(0,1, 0, 3), new Board().getMove(Board.BASE_BOARD, s0));
        Assert.assertEquals(new Move(-1, -1, -1, -1), new Board().getMove(s0,s0));

        String s1 = "11111111" +
                "01111111" +
                "00000000" +
                "10000000" +
                "01000000" +
                "00000000" +
                "10111111" +
                "11111111";

        String s2 = "11111111" +
                "01111111" +
                "00000000" +
                "10000000" +
                "00000000" +
                "00000000" +
                "10111111" +
                "11111111";

        Assert.assertEquals(new Move(1, 4, -1, -1), new Board().getMove(s1,s2));
    }

    @Test
    public void test_Board_BASE_BOARD() {
        Assert.assertEquals(Board.BASE_BOARD.length(), 64);
    }

    @Test
    public void test_Move() {
        Assert.assertEquals("0000", new Move(0,0,0,0).toArrayNotation());
        Assert.assertEquals("a1a1", new Move(0,0,0,0).toChessNotation());

        Assert.assertEquals("1234", new Move(1,2,3,4).toArrayNotation());
        Assert.assertEquals("b3d5", new Move(1,2,3,4).toChessNotation());

        Assert.assertFalse(new Move(1,1,1,-1).isComplete());
    }
}
