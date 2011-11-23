package iconcerto.hibernate.extender;

import iconcerto.extender.AbstractExtendedBundle;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.osgi.framework.Bundle;

/**
 * <b>Thread-safe
 * 
 * @author Ivan Pogudin <i.a.pogudin@gmail.com>
 *
 */
public class HibernateBundle extends AbstractExtendedBundle {
	
	private Set<String> classNames = new CopyOnWriteArraySet<String>();
	private Set<String> unmodifiableClassNames = Collections.unmodifiableSet(classNames);
	
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
		for (String className: rawClassNamesHeader.split(",")) {
			classNames.add(className);
		}		
	}
	
	public Set<String> getEntityClassNames() {
		return unmodifiableClassNames;
	}

	@Override
	protected boolean isValidNameForLoading(String name) {
		return classNames.contains(name);		
	}
	
}
