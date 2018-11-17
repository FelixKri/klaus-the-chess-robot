package dev.comhub.chessengine;

import java.util.LinkedList;

public class Board {

    public static final String BASE_BOARD = "11111111" +
            "11111111" +
            "00000000" +
            "00000000" +
            "00000000" +
            "00000000" +
            "11111111" +
            "11111111";

    private String prevBoard;
    private LinkedList<Move> moves;

    public Board() {
        prevBoard = BASE_BOARD;
        moves = new LinkedList<>();
    }

    public Board(String baseBoard) {
        this.prevBoard = baseBoard;//TODO: check string length
        moves = new LinkedList<>();
    }

    public void addBoard(String board) {
        if (board.length() != 64) {
            System.err.println("Board doesn't have correct size");//TODO: do we really need to print this ??
        }

        if(!prevBoard.equals(board)) {
            Move m = getMove(prevBoard, board);
            moves.add(m);
            prevBoard = board;
        }
    }

    public Move compileMove() {
        // cant get move if nothing significant happend
        // an instant move is not valid
        if(moves.size() < 2) {
            return null;
        }

        // normal move
        // edge case: player takes piece but puts it back
        if(moves.size() == 2) {

        }

        /* capture
         *
         * [0] -> piece is picked up
         * [1] -> to capture is picked up
         * [2] -> piece placed on others place
         */
        if(moves.size() == 3) {

        }

        /* castling
         *
         *  [0] -> king is picked up
         *  [1] -> rook is picked up
         *  [2] -> king is placed back
         *  [3] -> rook is placed back
         */
        if(moves.size() == 4) {

        }

        return null;
    }


    /**
     * Compares two boards and determines the move made
     *
     * @param from before move board
     * @param to after move board
     * @return the move
     */
    public Move getMove(String from, String to) {//TODO: why is this public ?? (because tests..)
        char[] newArray = from.toCharArray();
        char[] oldArray = to.toCharArray();

        int oldPos = -1;
        int newPos = -1;

        for (int i = 0; i < 64; i++) {
            if (oldArray[i] > newArray[i]) newPos = i;
            if (oldArray[i] < newArray[i]) oldPos = i;
        }

        int oldX = oldPos > -1 ? oldPos % 8 : Move.NOTHING;
        int oldY = oldPos > -1 ? oldPos / 8 : Move.NOTHING;
        int newX = newPos > -1 ? newPos % 8 : Move.NOTHING;
        int newY = newPos > -1 ? newPos / 8 : Move.NOTHING;

        return new Move(oldX, oldY, newX, newY);
    }

}
