package iconcerto.hibernate.extender;

import iconcerto.hibernate.extender.ExtendedBundle.Actions;

import org.osgi.framework.Bundle;

public interface ExtendedBundleFactory {

	ExtendedBundle newExtendedFactory(Bundle bundle, Actions action);
	
}
