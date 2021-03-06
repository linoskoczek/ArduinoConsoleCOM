package Model;

import gnu.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.TooManyListenersException;


public class Model implements SerialPortEventListener {
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 9600;
    public boolean isRunning = true;
    private SerialPort serialPort;
    private BufferedReader input;
    private OutputStream output;
    private LinkedList<String> messages = new LinkedList<>();

    Model() {
        openCommunicationPort(Connector.choosePort());
    }

    private void openCommunicationPort(CommPortIdentifier port) {
        try {
            serialPort = (SerialPort) port.open(this.getClass().getName(), TIME_OUT);
            serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (PortInUseException | UnsupportedCommOperationException | IOException | TooManyListenersException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Null value provided as a communication port. Cannot proceed.");
            System.exit(-1);
        }
    }

    private synchronized void disconnectPort() {
        if (serialPort != null) {
            serialPort.close();
            serialPort.removeEventListener();
        }
    }

    public synchronized void closeProgram() {
        isRunning = false;
        disconnectPort();
        System.exit(0);
    }

    public synchronized void write(String message) {
        try {
            output.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return messages.pollFirst();
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                messages.add(input.readLine());
            } catch (IOException e) {
                messages.add("Device has disconnected! Asking for a new port if available in 5 seconds...");
                disconnectPort();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                new Thread(Main::createNewModel).start();
            }
        }
    }
}
