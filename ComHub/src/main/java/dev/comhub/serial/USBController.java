package dev.comhub.serial;

import com.fazecast.jSerialComm.SerialPort;

import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * A wrapper around {@link com.fazecast.jSerialComm.SerialPort} it also made more generic.
 * It also features built in port selection.
 */
public class USBController {

    private SerialPort serialPort;
    private byte[] buffer;
    private int cursor = 0;
    private int dataLen = 0;
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
     *
     * @param baudRate baudrate of the connection
     */
    public void init(int baudRate) {
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
            if (choice < 0 || choice > ports.length - 1) {
                throw new Exception("Port number too big or too small");
            }

            serialPort = ports[choice];
            serialPort.setBaudRate(baudRate);
            boolean success = serialPort.openPort();

            if (!success) {
                throw new Exception("Couldn't open serial port");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        init = true;
    }

    /**
     * @return if a line is available to be read, see: {@link #nextLine()}
     */
    public boolean hasLine() {
        if (!init) return false;

        refillBuffer();
        return findNewLine() >= 0;
    }

    /**
     * Returns the next line from the buffer or null if there is nothing in the buffer.
     * If theres nothing in the buffer it will try to fill it.
     * A line ending is "\n", its not included in the string.
     *
     * @return next line in buffer or null
     */
    public String nextLine() {
        if (!init) return null;

        refillBuffer();

        //try to retrieve data
        int index = findNewLine();
        if (cursor < dataLen && index >= 0) {
            byte[] bytes = new byte[index - cursor];
            System.arraycopy(buffer, cursor, bytes, 0, bytes.length);
            cursor = index + 1;
            return new String(bytes, Charset.forName("US-ASCII"));
        }

        return null;
    }

    /**
     * Loads all available bytes that fit into the buffer,
     * if there is some data left in the buffer the new bytes will be appended.
     */
    private void refillBuffer() {
        //do we need to grab more data ?
        if (cursor == dataLen || findNewLine() == -1) {

            //push back any leftover data to front
            int offset = dataLen - cursor;

            /* theres a warning that we are using manual array copy,
             * ignore this since we can't use System.arraycopy() or Arrays.copyOfRange()
             * because those methods copy into a different array,
             * here we are pushing back in the same array
             */
            for (int i = 0; i < offset; i++) {
                buffer[i] = buffer[cursor + i];
            }

            int numRead = 0;

            //try to read new data into buffer
            if (serialPort.bytesAvailable() > 0) {
                try {
                    numRead = serialPort.readBytes(buffer, buffer.length - offset, offset);

                    if (numRead < 0) {
                        throw new Exception("Reading bytes has resulted in an error");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            cursor = 0;
            dataLen = offset + numRead;
        }
    }

    /**
     * Searches for '\n' in the buffer
     *
     * @return index or -1 if nothing is found
     */
    private int findNewLine() {
        for (int i = cursor; i < dataLen; i++) {
            if (buffer[i] == '\n') return i;
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
     * Sends the given string over the serialport.
     *
     * @param data string to send
     */
    public void write(String data) {
        if (!init) return;

        byte[] bytes = data.getBytes(Charset.forName("US-ASCII"));

        try {
            int numWrite = serialPort.writeBytes(bytes, bytes.length);

            if (numWrite < 0) {
                throw new Exception("Writing bytes has resulted in an error");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
