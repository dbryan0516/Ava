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
    }

    public void buildTargetMap(ArrayList<String> modules) {
        // TODO: make a ui package and handle all ui in there instead of here
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
        // TODO: see if closing out stream will cause problems
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
                // TODO: Should I cast here
                AvaModule module = (AvaModule) cons.newInstance(targetMap.get(moduleName));
                // Add into map by keyword
                // TODO: While testing see what key words get returned
                moduleMap.put(module.getKeyword(), module);
            } catch (NoSuchMethodError e) {
                // if no string constructor, try the default constructor
                try {
                    c = Class.forName("ava.modules." + moduleName);
                    Constructor<?> cons = c.getConstructor(String.class);
                    // Cast as interface
                    AvaModule module = (AvaModule) cons.newInstance(targetMap.get(moduleName));
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
     * Uses passive STT to listen for keyword, then uses online STT for the
     * actual command
     */
    public void listen() {

        // TODO: import sphinx or other onboard STT and set it to react to key
        // word
        // maybe pass them in from the executable

        // Start sphinx/or other on board STT; record every 2 seconds? on two
        // offset threads?
        // listen for this.NAME
        // if it is this.NAME then record for a few seconds, send to google STT
        // or other online service and then determine the keyword and module to
        // use.
        String command = "";
        if (!command.isEmpty()) {
            Scanner commandScanner = new Scanner(command);
            String keyword = commandScanner.next();
            moduleMap.get(keyword).execute(command);
        }

        // used for testing purposes
        moduleMap.get("Lights").execute("Some string");
    }
}
