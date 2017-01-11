package ava.modules;

import static org.junit.Assert.*;

import org.junit.Test;

public class LightsTest {

    @Test
    public void testExecute() {
        Lights lights = new Lights("192.168.0.1");
        lights.buildRequest("on");
        assertEquals("{\"system\":{\"set_relay_state\":{\"state\":1}}}", lights.getRequest());
        lights.send();
        // manually check if lights turned on
        // TODO: add check by getting the status and checking the relay state

        // lights.execute("irrelevant string");
    }

    @Test
    public void testBuildRequest() {
        Lights lights = new Lights("192.168.0.1");
        lights.buildRequest("on");
        assertEquals("{\"system\":{\"set_relay_state\":{\"state\":1}}}", lights.getRequest());
    }

    @Test
    public void testSend() {
        // TODO: DELETE?
        fail("Not yet implemented");
    }

    @Test
    public void testHandleResponse() {
        // TODO: see if executing a random string will cause handle response to
        // parse the status and then change the state of the relay
        fail("Not yet implemented");
    }

}
