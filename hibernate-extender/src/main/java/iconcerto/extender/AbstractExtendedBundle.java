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
public abstract class AbstractExtendedBundle implements ExtendedBundle {

	private final Bundle bundle;
	private volatile Actions action;
	
	public AbstractExtendedBundle(Bundle bundle, Actions action) {
		this.bundle = bundle;
		this.action = action;
	}

	@Override
	public int hashCode() {
		long value = getBundle().getBundleId();		
		return (int)(value ^ (value >>> 32));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		
		if (obj == this) return true;
		
		if (!this.getClass().equals(obj.getClass())) return false;
				
		return 
				getBundle().getBundleId() == 
				((AbstractExtendedBundle)obj).getBundle().getBundleId();
	}

	@Override
	public Bundle getBundle() {
		return bundle;
	}

	@Override
	public Actions getAction() {
		return action;
	}

	@Override
	public void markAsCompleted() {
		action = Actions.COMPLETED;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if (!isValidNameForLoading(name)) throw new ClassNotFoundException(name);			
		return getBundle().loadClass(name);
	}

	@Override
	public URL getResource(String name) {
		if (!isValidNameForLoading(name)) return null;
		return getBundle().getResource(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		if (!isValidNameForLoading(name)) return null;
		return getBundle().getResources(name);
	}
	
	protected abstract boolean isValidNameForLoading(String name);
	
}
