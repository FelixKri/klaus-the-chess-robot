package dev.comhub;

import dev.comhub.serial.USBController;

public class Main {

    public static void main(String[] args) {
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
