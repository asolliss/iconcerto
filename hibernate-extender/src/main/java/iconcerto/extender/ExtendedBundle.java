package iconcerto.extender;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.osgi.framework.Bundle;

/**
 * <b>Thread-safe
 * 
 * @author Ivan Pogudin <i.a.pogudin@gmail.com>
 *
 */
public interface ExtendedBundle {
	
	public static enum Actions {ADDING, REMOVING, COMPLETED};
	
	/**
	 * @return a native osgi bundle that can be extended
	 */
	Bundle getBundle();
	
	Actions getAction();
	
	/**
	 * This method checks if a bundle can be extended.
	 * The method can take much time and should not be invoked from the main thread. 
	 * @return
	 */
	boolean isValidForExtending();
	
	/**
	 * This method extends bundle to support specific features.
	 * The method can take much time and should not be invoked from the main thread.
	 * 
	 */
	void extend();
	
	void markAsCompleted();
	
	Class<?> loadClass(String name) throws ClassNotFoundException;
	
	URL getResource(String name);
	
	Enumeration<URL> getResources(String name) throws IOException;
}
