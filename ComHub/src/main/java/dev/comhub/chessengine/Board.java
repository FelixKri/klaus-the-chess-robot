package dev.comhub.chessengine;

/**
 * Stores the old board and converts the boards to their moves
 */
public class Board {

    private static String prevBoard = "";

    /**
     * This class shouldn't be instantiated
     */
    private Board() { }

    /**
     * Converts a board string to a move string by comparing the new and old boards
     * <p>
     * A1 = array[0][0]
     * H8 = array[7][7]
     *
     * @param newBoard a string of the new board
     * @return the move or null
     */
    public static String posArrayToMove(String newBoard) {
        if (newBoard.length() != 64) {
            System.err.println("Array doesn't have correct size");//TODO: do we really need to print this ??
            return null;
        }

        if (prevBoard.isEmpty()) {
            prevBoard = newBoard;
            return null;
        }

        char[] newArray = newBoard.trim().toCharArray();
        char[] oldArray = prevBoard.toCharArray();

        int oldPos = -1;
        int newPos = -1;

        for (int i = 0; i < 64; i++) {
            if (oldArray[i] > newArray[i]) oldPos = i;
            if (oldArray[i] < newArray[i]) newPos = i;
        }

        //found pos
        if (oldPos + newPos > 0) {
            String move = "";

            move += (char) ('a' + oldPos % 8);
            move += (char) ('1' + oldPos / 8);
            move += (char) ('a' + newPos % 8);
            move += (char) ('1' + newPos / 8);

            prevBoard = newBoard;
            return move;
        } else {
            return null;
        }
    }

    /**
     * Resets the previus String used in {@link #posArrayToMove(String)}
     */
    public static void reset() {
        prevBoard = "";
    }

    /**
     * Converts the move command from stockfish to an easier format for the arduino
     *
     * @return the new command
     */
    public static String moveToCommand(String move) {
        char[] cmdArr = move.toCharArray();

        String cmd = "";

        cmd += (int) (cmdArr[0] - 'a');
        cmd += (int) (cmdArr[1] - '1');
        cmd += (int) (cmdArr[2] - 'a');
        cmd += (int) (cmdArr[3] - '1');

        return cmd;
    }
}
