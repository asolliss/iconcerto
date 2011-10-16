package iconcerto.hibernate.extender;

import iconcerto.hibernate.extender.ExtendedBundle.Actions;

import org.osgi.framework.Bundle;

public class HibernateBundleFactory implements ExtendedBundleFactory {

	@Override
	public ExtendedBundle newExtendedFactory(Bundle bundle, Actions action) {
		HibernateBundle hibernateBundle = 
				new HibernateBundle(bundle, action);
		return hibernateBundle;
	}
	
}
