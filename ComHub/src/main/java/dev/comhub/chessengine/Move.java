package dev.comhub.chessengine;

/**
 * Stores a chess move
 */
public class Move {

    public static final int NOTHING = -1;

    protected int oldX, oldY;
    protected int newX, newY;

    public Move(int oldX, int oldY, int newX, int newY) {
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }

    /**
     * Returns the move in the style "d3b5"
     *
     * @return the move
     */
    public String toChessNotation() {
        String cmd = "";

        cmd += (char) (oldX + 'a');
        cmd += (char) (oldY + '1');
        cmd += (char) (newX + 'a');
        cmd += (char) (newY + '1');

        return cmd;
    }

    /**
     * Returns the move in the style "0134"
     *
     * @return the move
     */
    public String toArrayNotation() {
        String cmd = "";

        cmd += oldX;
        cmd += oldY;
        cmd += newX;
        cmd += newY;

        return cmd;
    }

    /**
     * Wather a move is ready to be submitted to stockfish
     *
     * @return is complete or is valid
     */
    public boolean isComplete() {
        return (oldX > NOTHING) && (oldY > NOTHING) && (newX > NOTHING) && (newY > NOTHING);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return oldX == move.oldX &&
                oldY == move.oldY &&
                newX == move.newX &&
                newY == move.newY;
    }

    @Override
    public String toString() {
        return "Move{" +
                "" + oldX +
                "," + oldY +
                ", " + newX +
                "," + newY +
                '}';
    }
}
