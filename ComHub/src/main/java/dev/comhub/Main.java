package dev.comhub;

import dev.comhub.serial.USBController;

public class Main {

    public static void main(String[] args) {
        USBController usbc = new USBController();
        usbc.init();

        while(true) {
            while(usbc.hasLine()) {
                System.out.println(usbc.nextLine());
            }

            try {
                Thread.sleep(789);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //usbc.close();
    }

}
