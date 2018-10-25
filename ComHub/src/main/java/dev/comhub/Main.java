package dev.comhub;

import com.fazecast.jSerialComm.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Ports: ");
        for (SerialPort p : SerialPort.getCommPorts()) {
            System.out.println(p);
        }

        System.out.println();

        SerialPort comPort = SerialPort.getCommPorts()[1];
        System.out.println(comPort);
        System.out.println(comPort.getBaudRate());
        System.out.println(comPort.getDescriptivePortName());
        System.out.println(comPort.getSystemPortName());
        System.out.println();

        comPort.openPort();
        try {
            while (true) {
                while (comPort.bytesAvailable() == 0);

                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
                System.out.println("Read " + numRead + " - " + new String(readBuffer));
                Thread.sleep(300);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        comPort.closePort();
    }

}
