package iconcerto.hibernate.extender;

import iconcerto.extender.AbstractExtendedBundle;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.Bundle;

/**
 * <b>Thread-safe
 * 
 * @author Ivan Pogudin <i.a.pogudin@gmail.com>
 *
 */
public class HibernateBundle extends AbstractExtendedBundle {
	
	private Set<String> classNames = new HashSet<String>();
	private Collection<String> unmodifiableClassNames = Collections.unmodifiableCollection(classNames);
	
	public HibernateBundle(Bundle bundle, Actions action) {
		super(bundle, action);
	}

	public static final String ENTITY_CLASSES_HEADER = "Entity-Classes";
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isValidForExtending() {
		Enumeration<String> headers = getBundle().getHeaders().keys();
		while (headers.hasMoreElements()) {
			if (ENTITY_CLASSES_HEADER.equals(headers.nextElement())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void extend() {
		String rawClassNamesHeader = (String) getBundle().getHeaders().get(ENTITY_CLASSES_HEADER);
		synchronized (classNames) {
			for (String className: rawClassNamesHeader.split(",")) {
				classNames.add(className);
			}
		}		
	}
	
	public Collection<String> getEntityClassNames() {
		return unmodifiableClassNames;
	}

	@Override
	protected boolean isValidNameForLoading(String name) {
		synchronized (classNames) {
			return classNames.contains(name);
		}		
	}
	
}
