package ava.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Abstract class for connecting to devices on your local network
 * 
 * @author dbryan (dylanmbryan@gmail.com)
 *
 */
public class LocalConnection {

    private InputStream inputStream;
    private OutputStream outputStream;
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

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
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

    public boolean write(byte[] request) {
        if (isClosed()) {
            reconnect();
        }

        try {
            outputStream.write(request);
            outputStream.flush();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public String read() throws IOException {
        // int count = 1;
        // int size = 1024;
        // int[] response = new int[size];
        //
        // try {
        // int in;
        // while ((in = inputStream.read()) != -1) {
        // //if the response buffer is too small
        // if (count == size) {
        // size = size * 2;
        // int[] dest = new int[size];
        // System.arraycopy(response, 0, dest, 0, response.length);
        // response = dest;
        // }
        // }
        // } catch (IOException e) {
        // e.printStackTrace();
        // return null;
        // }
        // // TODO: log size for analysis
        // return response;

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        reader.close();
        return out.toString();
    }

    /**
     * Opens a connection with the target device at the given IP/target through
     * the given port
     * 
     * @param target
     *            the target devices local IP address
     * @param port
     *            the port to connect on
     * @return true if the connection was built successfully, false otherwise
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
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
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
            outputStream.close();
            inputStream.close();
            socket.close();

            outputStream = null;
            inputStream = null;
            socket = null;
        } catch (IOException e) {
            // some error message

        }

    }

}
