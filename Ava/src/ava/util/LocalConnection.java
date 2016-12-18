package ava.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;

/**
 * Abstract class for connecting to devices on your local network
 * 
 * @author dbryan (dylanmbryan@gmail.com)
 *
 */
public abstract class LocalConnection {

	private BufferedReader responseReader;
	private PrintWriter requestWriter;
	private Socket socket;

	public abstract void buildRequest();

	public abstract void send();

	public abstract void handleResponse();

	/**
	 * Opens a connection with the target device at the given IP through the
	 * given port
	 * 
	 * @param target
	 *            the target devices local IP address
	 * @param port
	 *            the port to connect on
	 */
	public void buildConnection(String target, int port) {
		// String username, String pw
		// Add functionality for log in credentials

		this.socket = null;
		try {
			socket = new Socket(target, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("IO Error! Unable to establish input/output stream.");
			e.printStackTrace();
			return;
		}
	}

}
