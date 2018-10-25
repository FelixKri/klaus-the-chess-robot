package dev.comhub;

import dev.comhub.serial.USBController;

public class Main {

    public static void main(String[] args) {
        USBController usbc = new USBController();
        usbc.init();

        while(true) {
            String s = usbc.read();
            if(s != null) System.out.print(s);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //usbc.close();
    }

}
