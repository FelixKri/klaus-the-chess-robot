package dev.comhub.chessengine;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A wrapper around the stockfish engine and process
 *
 * Code was copied from https://github.com/NiflheimDev/Stockfish-Java/
 */
public class Stockfish implements ChessEngine {

    private Process process;
    private BufferedReader input;
    private BufferedWriter output;

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

    @Override
    public void close() throws IOException {
        try {
            sendCommand("quit");
        } finally {
            process.destroy();
            input.close();
            output.close();
        }
    }

    @Override
    public String getBestMove(int difficulty) {
        return getBestMove(getFen(), difficulty);
    }

    @Override
    public String getBestMove(String fen, int difficulty) {
        waitForReady();
        setOption("Skill Level", difficulty);

        waitForReady();
        sendCommand("position fen " + fen);

        waitForReady();
        sendCommand("go movetime 4000");

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

    @Override
    public List<String> getCheckers() {
        return getCheckers(getFen());
    }

    @Override
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

    @Override
    public void setFen(String fen) {
        waitForReady();
        sendCommand("position fen " + fen);
    }

    @Override
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

    @Override
    public String makeMove(String pgn) {
        return makeMove(getFen(), pgn);
    }

    @Override
    public String makeMove(String fen, String pgn) {
        waitForReady();
        sendCommand("position fen " + fen + " moves " + pgn);
        return getFen();
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

    public void setOption(String name, int value) {
        sendCommand("setoption name " + name + " value " + value);
    }

    private void waitForReady() {
        sendCommand("isready");
        readResponse("readyok");
    }
}
