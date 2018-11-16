package dev.comhub.test;

import dev.comhub.chessengine.Board;
import org.junit.Assert;
import org.junit.Test;

public class TestMain {

    @Test
    public void test_Board_moveToCommand() {
        Assert.assertEquals("0001", Board.moveToCommand("a1a2"));
        Assert.assertEquals("7776", Board.moveToCommand("h8h7"));
        Assert.assertEquals("6211", Board.moveToCommand("g3b2"));
    }

    @Test
    public void test_Board_posArrayToMove() {
        //init board
        String b = "1000000000000000000000000000000000000000000000000000000000000000";
        Board.posArrayToMove(b);

        b = "0100000000000000000000000000000000000000000000000000000000000000";
        Assert.assertEquals("a1b1", Board.posArrayToMove(b));

        b = "0000000000000000000000000000000000000000000000000000000000000001";
        Assert.assertEquals("b1h8", Board.posArrayToMove(b));
    }
}
