package ava.modules;

import ava.util.AvaModule;
import ava.util.WebConnection;

public class Weather extends WebConnection implements AvaModule {

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

}
