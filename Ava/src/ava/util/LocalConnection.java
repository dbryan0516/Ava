package ava.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Abstract class for connecting to devices on your local network
 * 
 * @author dbryan (dylanmbryan@gmail.com)
 *
 */
public class LocalConnection {

    private BufferedReader responseReader;
    private PrintWriter requestWriter;
    private Socket socket;
    private String target;
    private int port;

    public LocalConnection(String target, int port) {
        if (target == null || target.isEmpty() || port == 0) {
            throw new IllegalArgumentException("Target or Port empty");
        } else {

            this.target = target;
            this.port = port;
            buildConnection(target, port);
        }
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public BufferedReader getResponseReader() {
        return responseReader;
    }

    public PrintWriter getRequestWriter() {
        return requestWriter;
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * Returns if the socket is open or closed
     * 
     * @return true if the connection is closed
     */
    public boolean isClosed() {
        return socket.isClosed();
    }

    /**
     * Opens a connection with the target device at the given IP through the
     * given port
     * 
     * @param target
     *            the target devices local IP address
     * @param port
     *            the port to connect on
     */
    public boolean buildConnection(String target, int port) {
        // String username, String pw
        // Add functionality for log in credentials

        this.socket = null;
        try {
            socket = new Socket(target, port);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        try {
            requestWriter = new PrintWriter(socket.getOutputStream(), true);
            responseReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("IO Error! Unable to establish input/output stream.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Calls the close function and tries to build connection again.
     * 
     * @return true if connection was created, false otherwise
     */
    public boolean reconnect() {
        close();
        return buildConnection(target, port);
    }

    /**
     * Closes the connection
     */
    public void close() {
        try {
            responseReader.close();
            requestWriter.close();
            socket.close();

            responseReader = null;
            requestWriter = null;
            socket = null;
        } catch (IOException e) {
            // some error message

        }

    }

}
