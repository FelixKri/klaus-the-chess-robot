package dev.comhub.chessengine;

/**
 * Code from https://github.com/NiflheimDev/Stockfish-Java/
 */
public class StockfishInitException extends Exception {

    public StockfishInitException() {
        super();
    }

    public StockfishInitException(String message) {
        super(message);
    }

    public StockfishInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockfishInitException(Throwable cause) {
        super(cause);
    }
}
