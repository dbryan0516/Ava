package ava.manager;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

import ava.util.AvaModule;

/**
 * 
 * @author dbryan (dylambryan@gmail.com)
 */
public class AvaManager {

	public static final String NAME = "Ava";

	private HashMap<String, AvaModule> moduleMap;

	/**
	 * Constructor
	 */
	public AvaManager() {
		ArrayList<String> modules = new ArrayList<String>();
		// manually add them here until I can get them by all in the folder
//		modules.add("Weather");
		modules.add("Lights");
		buildModuleMap(modules);
		listen();
	}

	/**
	 * Reads each module and keyword to build the map for the
	 */
	private void buildModuleMap(ArrayList<String> modules) {
		this.moduleMap = new HashMap<String, AvaModule>();

		for (String moduleName : modules) {

			Class<?> c;
			try {
				c = Class.forName("ava.modules." + moduleName);
				Constructor<?> cons = c.getConstructor();
				// Cast as interface
				AvaModule module = (AvaModule) cons.newInstance();
				// Add into map by keyword
				moduleMap.put(module.getKeyword(), module);
			} catch (Exception e) {
				System.out.println("Module or Keyword Not Found for " + moduleName + "!");
				e.printStackTrace();
			}

		}
	}

	/**
	 * Uses passive STT to listen for keyword
	 */
	public void listen() {
		// TODO: import sphinx or other onboard STT and set it to react to key
		// word

		moduleMap.get("Lights").execute("Some string");
	}
}
