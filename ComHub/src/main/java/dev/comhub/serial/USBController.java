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
    private int dataLen = 0;
    private boolean init = false;

    public USBController() {
        this(50);
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
     * A line ending is "\n", its not included in the string.
     *
     * @return next line in buffer or null
     */
    public String nextLine() {//TODO: implement buffered line reading
        if (!init) return null;

        //is a line in the buffer
        if(cursor == dataLen || findNewLine() == -1) {
            System.out.println("> to buffer");
            dataLen = readToBuffer();
            cursor = 0;
        }

        //try to retrieve data
        int index = findNewLine();
        //System.out.println("> index " + index);
        if(cursor < dataLen && index >= 0) {
            System.out.println("> cursor " + cursor);
            System.out.println("> datalen " + dataLen);
            //System.out.println("> from buffer");

            byte[] bytes = new byte[index - cursor];
            System.arraycopy(buffer, cursor, bytes, 0, bytes.length);
            cursor = index + 1;
            return new String(bytes, Charset.forName("US-ASCII"));
        }

        return null;
    }

    /**
     * Loads all available bytes into the buffer,
     * if there is some data left in the buffer the new bytes will be appended.
     *
     * @return how much data is in the buffer
     */
    private int readToBuffer() {

        //push back any leftover data to front
        int index = findNewLine();

        for(int i = 0; i < index - cursor; i++) {
            buffer[i] = buffer[cursor + i];
        }

        int offset = (index - cursor) < 0 ? 0 : (index - cursor);

        //try to read new data into buffer
        //System.out.println("> avail " + serialPort.bytesAvailable()  + " bytes");
        if (serialPort.bytesAvailable() > 0) {
            try {
                int numRead = serialPort.readBytes(buffer, buffer.length - offset, offset);
                //System.out.println("> read " + numRead + " bytes");

                if (numRead < 0) {
                    throw new Exception("Reading bytes has resulted in an error");
                }

                return offset + numRead;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return offset;
    }

    /**
     * Searches for '\n' in the buffer
     *
     * @return index or -1 if nothing is found
     */
    private int findNewLine() {
        for(int i = cursor; i < dataLen; i++) {
            if(buffer[i] == '\n') return i;
        }

        return -1;
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
                "baudrate=" + serialPort.getBaudRate() +
                '}';
    }
}
