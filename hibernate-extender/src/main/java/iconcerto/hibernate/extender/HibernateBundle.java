package iconcerto.hibernate.extender;

import org.osgi.framework.Bundle;

public class HibernateBundle implements ExtendedBundle {
	
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

	@Override
	public boolean isValidForExtending() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
