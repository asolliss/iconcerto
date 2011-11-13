package iconcerto.hibernate.extender;

import java.util.Enumeration;

import org.osgi.framework.Bundle;

public class HibernateBundle implements ExtendedBundle {
	
	public static final String ENTITY_CLASSES_HEADER = "Entity-Classes";
	
	private Bundle bundle;
	private Actions action;
	private States state;
	
	public HibernateBundle(Bundle bundle, Actions action) {
		this.bundle = bundle;
		this.action = action;
		this.state = States.UNATTACHED;
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
	public States getState() {
		return state;
	}

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
	
}
