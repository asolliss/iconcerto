package iconcerto.extender;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <b>Thread-safe
 * 
 * This class implements ClassLoader with special behavior.
 * 
 * @author Ivan Pogudin <i.a.pogudin@gmail.com>
 *
 */
public class ExtenderClassLoader extends ClassLoader {

	private volatile ClassLoader defaultClassLoader = null;
	private Set<ExtendedBundle> bundles = new CopyOnWriteArraySet<ExtendedBundle>();	

	public ClassLoader getDefaultClassLoader() {
		return defaultClassLoader;
	}

	public void setDefaultClassLoader(ClassLoader defaultClassLoader) {
		this.defaultClassLoader = defaultClassLoader;
	}
	
	public void addBundle(ExtendedBundle bundle) {
		bundles.add(bundle);
	}
	
	public void removeBundle(ExtendedBundle bundle) {
		bundles.remove(bundle);
	}

	/**
	 * This method tries to load class from Bundle.loadClass methods.
	 * If class is not found then this method will try to load it from defaultClassLoader.loadClass method.
	 * If defaultClassLoader.loadClass method can't load a necessary class then ClassNotFoundException will be thrown.
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> c = null;
		for (ExtendedBundle bundle: bundles) {
			try {
				c = bundle.loadClass(name);
				break;
			}
			catch (ClassNotFoundException e) {}
		}
		
		if (c == null && getDefaultClassLoader() != null) {
			try {
				c = getDefaultClassLoader().loadClass(name);
			}
			catch (ClassNotFoundException e) {}
		}
		
		if (c == null) throw new ClassNotFoundException(name);
			
		return c;
	}

	@Override
	public URL getResource(String name) {
		URL resource = null;
		
		for (ExtendedBundle bundle: bundles) {
			resource = bundle.getResource(name);
			if (resource != null) break;
		}
		
		if (resource == null && getDefaultClassLoader() != null) {
			resource = getDefaultClassLoader().getResource(name);
		}
		
		return resource;
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		SimpleEnumeration<URL> urls = new SimpleEnumeration<URL>();
		
		for (ExtendedBundle bundle: bundles) {
			for (
					Enumeration<URL> e = bundle.getResources(name); 
					e != null && e.hasMoreElements(); 
					) {
				urls.add(e.nextElement());				
			}
		}
		
		if (getDefaultClassLoader() != null) {
			for (
					Enumeration<URL> e = getDefaultClassLoader().getResources(name); 
					e.hasMoreElements();
					) {
				urls.add(e.nextElement());				
			}
		}
		
		return urls;
	}
	
	private static class SimpleEnumeration<T> extends ArrayList<T> implements Enumeration<T> {

		private static final long serialVersionUID = 54011968331343529L;
		private Iterator<T> currentIterator;
		
		@Override
		public boolean hasMoreElements() {	
			if (currentIterator == null) {
				currentIterator = iterator();
			}
			return currentIterator.hasNext();
		}

		@Override
		public T nextElement() {
			return currentIterator.next();
		}
		
	}
	
}
