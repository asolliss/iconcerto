package iconcerto.hibernate.extender;

import iconcerto.extender.ExtendedBundle;
import iconcerto.extender.ExtendedBundleFactory;
import iconcerto.extender.ExtendedBundle.Actions;

import org.osgi.framework.Bundle;

public class HibernateBundleFactory implements ExtendedBundleFactory {

	@Override
	public ExtendedBundle newExtendedFactory(Bundle bundle, Actions action) {
		HibernateBundle hibernateBundle = 
				new HibernateBundle(bundle, action);
		return hibernateBundle;
	}
	
}
