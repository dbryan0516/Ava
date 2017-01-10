package ava.modules;

import static org.junit.Assert.*;

import org.junit.Test;

public class LightsTest {

	@Test
	public void testExecute() {
		Lights lights = new Lights("192.168.0.1");
		lights.execute("random string");
	}

	@Test
	public void testBuildRequest() {
		fail("Not yet implemented");
	}

	@Test
	public void testSend() {
		fail("Not yet implemented");
	}

	@Test
	public void testHandleResponse() {
		fail("Not yet implemented");
	}

}
