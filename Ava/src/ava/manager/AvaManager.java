package ava.manager;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import ava.util.AvaModule;

/**
 * 
 * @author dbryan (dylambryan@gmail.com)
 */
public class AvaManager {

	public static final String NAME = "Ava";

	// Dictionary/Map from the module to the target ip, site, etc
	private HashMap<String, String> targetMap;
	// Map from keyword to AvaModule
	private HashMap<String, AvaModule> moduleMap;

	/**
	 * Constructor
	 */
	public AvaManager() {
		ArrayList<String> modules = new ArrayList<String>();
		// manually add them here until I can get them by all in the folder
		// modules.add("Weather");
		modules.add("Lights");
		buildTargetMap(modules);
		buildModuleMap(modules);
		listen();
	}

	public void buildTargetMap(ArrayList<String> modules) {
		// TODO: make 'are you sure?' method; type module you want to change
		// target ip, and eventually make it dynamically check the targets for
		// local connections
		// TODO: do i need to set up ports as well? could do in same method
		this.targetMap = new HashMap<String, String>();
		Scanner in = new Scanner(System.in);
		PrintStream out = new PrintStream(System.out);
		out.println("Please enter the target IP or Address for all modules installed.");
		for (String modulename : modules) {
			out.print("What is the target for " + modulename + ": ");
			String target = in.nextLine();
			targetMap.put(modulename, target);
		}
		in.close();
		// TODO: see if closing out will cause problems
	}

	/**
	 * Reads each module and keyword to build the map for the
	 */
	private void buildModuleMap(ArrayList<String> modules) {
		this.moduleMap = new HashMap<String, AvaModule>();

		for (String moduleName : modules) {

			Class<?> c;
			try {
				// TODO: see if not having .java makes a difference
				c = Class.forName("ava.modules." + moduleName);
				Constructor<?> cons = c.getConstructor(String.class);
				// Cast as interface
				AvaModule module = (AvaModule) cons.newInstance();
				// Add into map by keyword
				moduleMap.put(module.getKeyword(), module);
			} catch (NoSuchMethodError e) {
				// if no string constructor, try the default constructor
				try {
					c = Class.forName("ava.modules." + moduleName);
					Constructor<?> cons = c.getConstructor(String.class);
					// Cast as interface
					AvaModule module = (AvaModule) cons.newInstance();
					// Add into map by keyword
					moduleMap.put(module.getKeyword(), module);
				} catch (Exception f) {
					System.out.println("Module or Keyword Not Found for " + moduleName + "!");
					f.printStackTrace();
				}
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
		// maybe pass them in from the executable

		moduleMap.get("Lights").execute("Some string");
	}
}
