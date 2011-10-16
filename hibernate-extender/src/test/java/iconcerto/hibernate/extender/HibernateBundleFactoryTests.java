package iconcerto.hibernate.extender;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import iconcerto.hibernate.extender.ExtendedBundle.Actions;
import iconcerto.hibernate.extender.ExtendedBundle.States;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;

public class HibernateBundleFactoryTests {

	private HibernateBundleFactory hibernateBundleFactory;
	
	@Before
	public void setUp() {
		hibernateBundleFactory = new HibernateBundleFactory();
	}
	
	@Test
	public void defaultExtendedBundleStates() {
		Bundle mockedBundle = mock(Bundle.class);
		ExtendedBundle extendedBundle = 
				hibernateBundleFactory.newExtendedFactory(
						mockedBundle, 
						Actions.REMOVING
						);
		
		assertNotNull(extendedBundle);
		
		assertEquals(Actions.REMOVING, extendedBundle.getAction());
		
		assertEquals(States.UNATTACHED, extendedBundle.getState());
		
		assertNotNull(extendedBundle.getBundle());
		assertSame(mockedBundle, extendedBundle.getBundle());
		
	}
	
}
