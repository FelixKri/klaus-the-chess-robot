package dev.comhub.chessengine;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A wrapper around the stockfish engine
 * it also has predefined functions to communicate
 *
 * Code was copied from https://github.com/NiflheimDev/Stockfish-Java/
 */
public class Stockfish {

    private Process process;
    private BufferedReader input;
    private BufferedWriter output;

    /**
     * @param path the path to the engine executable
     */
    public Stockfish(String path) {
        try {
            ProcessBuilder ps = new ProcessBuilder(path);
            ps.redirectErrorStream(true);
            process = ps.start();

            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        try {
            sendCommand("quit");
        } finally {
            process.destroy();
            input.close();
            output.close();
        }
    }

    /**
     * Gets the best possible move depending on the current chess board
     *
     * @param difficulty difficulty of the engine
     * @param movetime amount of time, in ms, that stockfish should use to find a move
     * @return
     */
    public String getBestMove(int difficulty, int movetime) {
        return getBestMove(getFen(), difficulty, movetime);
    }

    /**
     * Gets the best possible move depending on the current chess board
     *
     * @param fen chess board
     * @param difficulty difficulty of the engine
     * @param movetime amount of time, in ms, that stockfish should use to find a move
     * @return the best move
     */
    public String getBestMove(String fen, int difficulty, int movetime) {
        waitForReady();
        setOption("Skill Level", difficulty);

        waitForReady();
        sendCommand("position fen " + fen);

        waitForReady();
        sendCommand("go movetime " + movetime);

        String bestmove = "";
        List<String> response = readResponse("bestmove");

        for (int i = response.size() - 1; i >= 0; i--) {
            String line = response.get(i);
            if (line.startsWith("bestmove")) {
                bestmove = line.substring("bestmove ".length());
                break;
            }
        }

        return bestmove.split("\\s+")[0];
    }

    /**
     * Returns a list of possible positions to place a piece to achieve a checkmate
     * it uses the current fen
     *
     * @return list of possible positions to place a piece to achieve a checkmate
     */
    public List<String> getCheckers() {
        return getCheckers(getFen());
    }

    /**
     * Returns a list of possible positions to place a piece to achieve a checkmate
     *
     * @param fen the current board
     * @return list of possible positions to place a piece to achieve a checkmate
     */
    public List<String> getCheckers(String fen) {
        waitForReady();
        sendCommand("position fen " + fen);

        waitForReady();
        sendCommand("d");

        String[] checkers = new String[0];
        List<String> response = readResponse("Checkers:");

        for (int i = response.size() - 1; i >= 0; i--) {
            String line = response.get(i);
            if (line.startsWith("Checkers: ")) {
                checkers = line.substring("Checkers: ".length()).split("\\s+");
                break;
            }
        }

        return Arrays.stream(checkers).filter(e -> e.length() == 2).collect(Collectors.toList());
    }

    public void setFen(String fen) {
        waitForReady();
        sendCommand("position fen " + fen);
    }

    public String getFen() {
        waitForReady();
        sendCommand("d");

        String fen = "";
        List<String> response = readResponse("Checkers:");

        for (int i = response.size() - 1; i >= 0; i--) {
            String line = response.get(i);
            if (line.startsWith("Fen: ")) {
                fen = line.substring("Fen: ".length());
                break;
            }
        }
        return fen;
    }

    public String makeMove(String pgn) {
        return makeMove(getFen(), pgn);
    }

    public String makeMove(String fen, String pgn) {
        waitForReady();
        sendCommand("position fen " + fen + " moves " + pgn);
        return getFen();
    }

    public void setOption(String name, int value) {
        sendCommand("setoption name " + name + " value " + value);
    }

    private List<String> readResponse(String expected) {
        try {
            List<String> lines = new ArrayList<>();
            while (true) {
                String line = input.readLine();
                lines.add(line);

                if (line.startsWith(expected))
                    break;
            }
            return lines;
        } catch (IOException e) {
            throw new StockfishException(e);
        }
    }

    private void sendCommand(String command) {
        try {
            output.write(command + "\n");
            output .flush();
        } catch (IOException e) {
            throw new StockfishException(e);
        }
    }

    private void waitForReady() {
        sendCommand("isready");
        readResponse("readyok");
    }
}
