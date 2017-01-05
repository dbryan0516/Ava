package ava.modules;

import ava.util.AvaModule;
import ava.util.WebConnection;

public class Weather implements AvaModule {

	/* The keyword to be mapped to the module */
	private String keyword = "Weather";

	@Override
	public void execute(String commandString) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getKeyword() {
		return this.keyword;
	}

	@Override
	public void buildRequest(String string) {
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
