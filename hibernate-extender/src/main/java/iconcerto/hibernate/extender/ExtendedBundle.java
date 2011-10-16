package iconcerto.hibernate.extender;

import org.osgi.framework.Bundle;

public interface ExtendedBundle {
	
	public static enum Actions {ADDING, REMOVING, COMPLETED};
	public static enum States {ATTACHED, UNATTACHED};
	
	Bundle getBundle();
	
	Actions getAction();
	
	States getState();
	
	boolean isValidForExtending();
	
}
