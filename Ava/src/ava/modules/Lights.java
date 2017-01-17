package ava.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
        buildRequest("on");
        conn = new LocalConnection(target, PORT);
        send();
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

    // def encrypt(string):
    // key = 171
    // result = "\0\0\0\0"
    // for i in string:
    // a = key ^ ord(i)
    // key = a
    // result += chr(a)
    // return result

    // private static byte[] encrypt(String request) {
    // byte[] bArr = new byte[request.length()];
    // // Byte key = (byte) 171;
    // if (bArr != null && bArr.length > 0) {
    // int key = -81; // originally -81
    // for (int i = 0; i < bArr.length; i++) {
    // byte b = (key ^ (int) bArr[i]);
    // key = bArr[i];
    // bArr[i] = b;
    // }
    // }
    //
    // int key = 171;
    // String result = "\0\0\0\0";
    //
    // return bArr;
    // }

    private byte[] encrypt(String command) {

        int[] buffer = new int[command.length()];
        int key = 0xAB; // may change this to 171
        for (int i = 0; i < command.length(); i++) {

            buffer[i] = command.charAt(i) ^ key;
            key = buffer[i];
        }

        // problem lies in the 'header'. command should follow 4 null bytes.
        // Currently only follows 3 and some random one
        byte[] bufferHeader = ByteBuffer.allocate(4).putInt(command.length()).array();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferHeader.length + buffer.length).put(bufferHeader);
        for (int in : buffer) {

            byteBuffer.put((byte) in);
        }
        return byteBuffer.array();
    }

    private static byte[] decrypt(byte[] bArr) {
        // TODO: Make sure encrypt works and then Convert code from python
        // if (bArr != null && bArr.length > 0) {
        // int key = -85;
        // for (int i = 0; i < bArr.length; i++) {
        // byte b = (byte) (key ^ bArr[i]);
        // key = bArr[i];
        // bArr[i] = b;
        // }
        // }

        return bArr;
    }

    @Override
    public void buildRequest(String string) {
        if (string.equals("status")) {
            request = "{'system':{'get_sysinfo':{}}}";
        } else if (string.equals("on")) {
            // TODO: See if I can build request without json objects
            // request = "{\"system\":{\"set_relay_state\":{\"state\":1}}}";
            JSONObject j = new JSONObject();
            j.put("state", 1);
            JSONObject k = new JSONObject();
            k.put("set_relay_state", j);
            JSONObject l = new JSONObject();
            l.put("system", k);
            request = l.toJSONString();
        } else if (string.equals("off")) {
            request = "{'system':{'set_relay_state':{'state':0}}}";
        }
        // TODO: add state and dynamically build based on state response

    }

    @Override
    public void send() {
        if (conn.isClosed()) {
            System.out.println("reconnecting");
            conn.reconnect();
        }

        // write binary to file to compare with python encryption binary
        File f = new File("test.txt");
        try {
            FileOutputStream out = new FileOutputStream("test.txt");
            out.write(encrypt(request));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        PrintWriter requestWriter = conn.getRequestWriter();
        requestWriter.print(encrypt(request));
        requestWriter.flush();
        System.out.println("command sent");
    }

    @Override
    public void handleResponse() {

        JSONParser parser = new JSONParser();

        String response = null;
        try {
            response = conn.getResponseReader().readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            response = new String(decrypt(response.getBytes()), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Object obj = null;
        try {
            obj = parser.parse(response);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject jsonObject = (JSONObject) obj;

        // Breakpoint to determine structure

    }

}
