package iconcerto.hibernate.extender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import iconcerto.extender.ExtendedBundle;
import iconcerto.extender.ExtendedBundle.Actions;

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
		
		assertNotNull(extendedBundle.getBundle());
		assertSame(mockedBundle, extendedBundle.getBundle());
		
	}
	
}
