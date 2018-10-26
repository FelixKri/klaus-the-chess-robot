package dev.comhub;

import dev.comhub.serial.USBController;

public class Main {

    public static void main(String[] args) {
        USBController usbc = new USBController();
        usbc.init();

        while(true) {
            String s = usbc.nextLine();
            if(s != null) System.out.println("\t\t\t\t\t\t" + s);
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //usbc.close();
    }

}
