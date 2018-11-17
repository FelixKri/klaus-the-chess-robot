package dev.comhub;

import dev.comhub.chessengine.__OldBoard;
import dev.comhub.chessengine.Stockfish;
import dev.comhub.serial.USBController;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Board_example();
    }

    public static void Board_example() {
        USBController usbc = new USBController();
        usbc.init(38400);

        while (true) {
            while (usbc.hasLine()) {
                String l = usbc.nextLine();
                String move = __OldBoard.posArrayToMove(l);
                if(move != null) {
                    System.out.println(move);
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void Stockfish_example() {
        String eng = "C:\\Users\\Thomas\\Desktop\\stockfish-9-win\\Windows\\stockfish_9_x64.exe";
        Stockfish s = new Stockfish(eng);
        s.setOption("Threads", 4);

        while (true) {

            //white
            String fen = s.getFen();
            Scanner scn = new Scanner(System.in);
            String move = s.getBestMove(0, 100);
            s.makeMove(fen, move);

            /*for (String d : s.getCheckers()) {
                System.out.println(d);
            }*/

            System.out.println(fen + "\n  ~  " + move + "\n");

            if(move.equals("(none)")) {
                break;
            }

            //black
            fen = s.getFen();
            move = s.getBestMove(20, 9000);
            s.makeMove(fen, move);

            System.out.println(fen);
            /*for (String d : s.getCheckers()) {
                System.out.println(d);
            }*/

            System.out.println("  ~  " + move + "\n");
        }
    }

    public static void USBController_example() {
        USBController usbc = new USBController();
        usbc.init(38400);

        System.out.println("It takes some time until the first data is echoed\n");

        while (true) {
            while (usbc.hasLine()) {
                System.out.println(usbc.nextLine());
            }

            usbc.write("arm " + getCommand() + "\n");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //usbc.close();
    }

    //example function
    public static String getCommand() {
        return "x:" + Math.round(Math.random() * 1000) +
                " y:" + Math.round(Math.random() * 1000) +
                " z:" + Math.round(Math.random() * 1000);
    }

}
