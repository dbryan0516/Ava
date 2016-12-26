package ava.modules;

import ava.util.AvaModule;
import ava.util.LocalConnection;

public class Lights implements AvaModule {

	/* The keyword to be mapped to the module */
	private String keyword = "Lights";

	/* The IP for the device */
	private String target;

	@Override
	public void execute(String commandString) {
		// command string is not used for this because I only care about turning
		// on/off the lights
		LocalConnection conn = new LocalConnection("192.168.0.19", 9999);
	}

	@Override
	public String getKeyword() {
		return this.keyword;
	}

	private static byte[] encrypt(byte[] bArr) {
		if (bArr != null && bArr.length > 0) {
			int key = -85;
			for (int i = 0; i < bArr.length; i++) {
				byte b = (byte) (key ^ bArr[i]);
				key = bArr[i];
				bArr[i] = b;
			}
		}

		return bArr;
	}

	@Override
	public void buildRequest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void send() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleResponse() {
		// TODO Auto-generated method stub

	}

}
