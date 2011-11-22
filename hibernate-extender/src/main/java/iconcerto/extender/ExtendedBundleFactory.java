package iconcerto.extender;

import iconcerto.extender.ExtendedBundle.Actions;

import org.osgi.framework.Bundle;

public interface ExtendedBundleFactory {

	ExtendedBundle newExtendedFactory(Bundle bundle, Actions action);
	
}
