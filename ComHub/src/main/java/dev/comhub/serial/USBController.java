package dev.comhub.serial;

import com.fazecast.jSerialComm.SerialPort;

import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * A wrapper around {@link com.fazecast.jSerialComm.SerialPort} it also made more generic.
 * It also features built in port selection.
 * <p>
 * To use this class baudrate MUST be 9600!
 */
public class USBController {

    private SerialPort serialPort;
    private byte[] buffer;
    private int cursor = 0;
    private boolean init = false;

    public USBController() {
        this(256);
    }

    /**
     * @param bufferSize size of the read buffer
     */
    public USBController(int bufferSize) {
        buffer = new byte[bufferSize];
    }

    /**
     * Lets the user choose which com port to use and
     * initializes the port.
     */
    public void init() {
        if (init) return;//TODO: should the user be notified that the controller isnt initialized ?

        SerialPort[] ports = SerialPort.getCommPorts();

        System.out.println("Please choose a serial port:");
        for (int i = 0; i < ports.length; i++) {
            System.out.printf("[%d] => [%s, %s]\n", i,
                    ports[i].getPortDescription(), ports[i].getSystemPortName());
        }

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        try {
            if (choice < 0 || choice > ports.length)
                throw new Exception("Port number too big or too small");

            serialPort = ports[choice];
            serialPort.openPort();

        } catch (Exception e) {
            e.printStackTrace();
        }

        init = true;
    }

    /**
     * Returns the next line from the buffer or null if there is nothing in the buffer.
     * If theres nothing in the buffer it will try to fill it.
     * A line ending is "\n"
     */
    public String nextLine() {//TODO: implement buffered line reading
        return null;
    }

    /**
     * Reads every single byte available and puts it into a string,
     * if nothing is available null is returned
     *
     * @return the read string
     */
    public String read() {
        if (!init) return null;

        if (serialPort.bytesAvailable() > 0) {
            byte[] bytes = new byte[serialPort.bytesAvailable()];

            try {
                int numRead = serialPort.readBytes(bytes, bytes.length);
                if (numRead < 0) {
                    throw new Exception("Reading bytes has resulted in an error");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new String(bytes, Charset.forName("US-ASCII"));
        }

        return null;
    }

    /**
     * Must be called at the end of the program to cleanup resources and
     * close the serialport.
     */
    public void close() {
        if (!init) return;
        serialPort.closePort();
    }

    public boolean isInitialized() {
        return init;
    }

    @Override
    public String toString() {
        if (!init) return "USBController{not initialized}";

        return "USBController{" +
                "name=" + serialPort.getDescriptivePortName() + ", " +
                "sysname=" + serialPort.getSystemPortName() + ", " +
                "description=" + serialPort.getPortDescription() + ", " +
                "baudrate=" + serialPort.getBaudRate() + ", " +
                '}';
    }
}
