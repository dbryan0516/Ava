package ava.modules;

import java.nio.ByteBuffer;
import org.json.*;

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
        // TODO: temp hold 192.168.0.2
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

    private String decrypt(byte[] response) {

        int in;
        int key = 171;
        int nextKey;
        StringBuilder sb = new StringBuilder();
        for (int i = 4; i < response.length; i++) {
            in = response[i];
            nextKey = in;
            in = key ^ in;
            sb.append((char) in);
            key = nextKey;

        }

        System.out.println("{" + sb.toString().substring(1, sb.length() - 1));
        System.out.println(sb.length());
        return "{" + sb.toString().substring(1, sb.length() - 1);

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
            System.out.println("Reconnecting to HS100");
            conn.reconnect();
        }

        conn.writeBytes(encrypt(request));
    }

    @Override
    public void handleResponse() {

        // get the encrypted response
        byte[] enResponse;
        enResponse = conn.readBytes();
        // conn.close();
        // decrypt it
        String response;
        response = decrypt(enResponse);

        // turn to JSON object; get status

        int relayState = -1;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonObject2 = ((JSONObject) jsonObject.getJSONObject("system").getJSONObject("get_sysinfo"));
            relayState = (int) jsonObject2.get("relay_state");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (relayState == 0) {
            // switch is off; lets turn it on
            buildRequest("on");
            System.out.println(request);
        } else if (relayState == 1) {
            // switch is on; lets turn it off
            buildRequest("off");
            System.out.println(request);
        } else {
            // error
            System.out.println("Unable to get relay state");
        }

        conn.reconnect();
        // send command to change relay state
        send();

        System.out.println("message sent");

    }

}
