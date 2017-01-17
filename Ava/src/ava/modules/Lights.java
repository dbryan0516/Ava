package ava.modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ava.util.AvaModule;
import ava.util.LocalConnection;

public class Lights implements AvaModule {

    /* The keyword to be mapped to the module */
    private String keyword = "Lights";
    public static final int PORT = 9999;

    /* The IP for the device */
    private String target;
    private LocalConnection conn;
    private String request;

    public Lights(String target) {
        this.target = target;
    }

    @Override
    public void execute(String commandString) {
        // command string is not used for this because I only care about turning
        // on/off the lights
        buildRequest("status");
        conn = new LocalConnection(target, PORT);
        send();
        handleResponse();
        conn.close();
    }

    @Override
    public String getKeyword() {
        return this.keyword;
    }

    public String getRequest() {
        return this.request;
    }

    // Python code to encrypt using static key.
    // Python code works, Java does not

    private byte[] encrypt(String command) {

        byte nullByte = (byte) '\0';
        int[] buffer = new int[command.length()];
        int key = 0xAB; // may change this to 171
        for (int i = 0; i < command.length(); i++) {

            buffer[i] = command.charAt(i) ^ key;
            key = buffer[i];
        }

        // problem lies in the 'header'. command should follow 4 null bytes.
        // Currently only follows 3 and some random one
        // byte[] bufferHeader =
        // ByteBuffer.allocate(4).putInt(command.length()).array();
        // ByteBuffer byteBuffer = ByteBuffer.allocate(bufferHeader.length +
        // buffer.length).put(bufferHeader);
        int headerLength = 4;
        ByteBuffer byteBuffer = ByteBuffer.allocate(buffer.length + headerLength);
        for (int i = 0; i < headerLength; i++) {
            byteBuffer.put(nullByte);
        }
        for (int in : buffer) {

            byteBuffer.put((byte) in);
        }
        return byteBuffer.array();
    }

    private String decrypt(String response) {
        // TODO: Make sure encrypt works and then Convert code from python
        // if (bArr != null && bArr.length > 0) {
        // int key = -85;
        // for (int i = 0; i < bArr.length; i++) {
        // byte b = (byte) (key ^ bArr[i]);
        // key = bArr[i];
        // bArr[i] = b;
        // }
        // }
        File f = new File("Pre-decrypt.txt");
        try {
            PrintWriter out = new PrintWriter(f);
            out.print(response);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int in;
        int key = 171;
        int nextKey;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < response.length(); i++) {
            in = (int) response.charAt(i);
            nextKey = in;
            in = in ^ key;
            key = nextKey;
            sb.append((char) in);
        }
        // System.out.println(sb.toString());
        return "{" + sb.toString().substring(5);

    }

    @Override
    public void buildRequest(String string) {
        if (string.equals("status")) {
            request = "{\"system\":{\"get_sysinfo\":{}}}";
        } else if (string.equals("on")) {
            request = "{\"system\":{\"set_relay_state\":{\"state\":1}}}";
        } else if (string.equals("off")) {
            request = "{\"system\":{\"set_relay_state\":{\"state\":0}}}";
        }
        // TODO: add state and dynamically build based on state response

    }

    @Override
    public void send() {
        if (conn.isClosed()) {
            System.out.println("reconnecting");
            conn.reconnect();
        }

        conn.write(encrypt(request));
    }

    @Override
    public void handleResponse() {

        JSONParser parser = new JSONParser();

        String response = null;
        try {
            response = decrypt(conn.read());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }

        Object obj = null;
        try {
            obj = parser.parse(response);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject jsonObject = (JSONObject) obj;
        System.out.println(jsonObject.toJSONString());

        // Breakpoint to determine structure

    }

}
