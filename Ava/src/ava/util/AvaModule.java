package ava.util;

/**
 * 
 * @author dbryan (dylanmbryan@gmail.com)
 *
 */
public interface AvaModule {

	/**
	 * Determines the action to take from the command string passed in
	 * 
	 * @param commandString
	 *            the full command string from the STT engine
	 */
	public void execute(String commandString);

	/**
	 * Returns the keyword for the module to get mapped to the object
	 * 
	 * @return keyword the word to be mapped
	 */
	public String getKeyword();

	public void buildRequest();

	public void send();

	public void handleResponse();

}
