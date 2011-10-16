package iconcerto.hibernate.extender;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import iconcerto.hibernate.extender.ExtendedBundle.Actions;

import org.junit.Before;
import org.junit.Test;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;

public class ExtenderTests {
	
	private Extender extender;
	
	@Before
	public void setUp() {
		extender = new Extender();
		extender.setExtendedBundleFactory(
				new ExtendedBundleFactory() {					
					@Override
					public ExtendedBundle newExtendedFactory(Bundle bundle, Actions action) {
						ExtendedBundle mockedExtendedBundle =
								mock(ExtendedBundle.class);
						when(mockedExtendedBundle.getBundle()).thenReturn(bundle);
						when(mockedExtendedBundle.getAction()).thenReturn(action);
						when(mockedExtendedBundle.isValidForExtending()).thenReturn(true);
						return mockedExtendedBundle;
					}
				}
				);
	}

	@Test
	public void bundleWithCorrectEvent() {
		Bundle mockedBundle = mock(Bundle.class);
		
		BundleEvent mockedEvent = mock(BundleEvent.class);
		when(mockedEvent.getBundle()).thenReturn(mockedBundle);
		when(mockedEvent.getType()).thenReturn(BundleEvent.STARTED);
		
		extender.bundleChanged(mockedEvent);
		
		assertEquals(1, extender.getBundles().size());
		ExtendedBundle bundle = extender.getBundles().peek();
		assertNotNull(bundle);
		assertSame(mockedBundle, bundle.getBundle());
	}
	
	@Test
	public void bundleWithIncorrectEvent() {
		Bundle mockedBundle = mock(Bundle.class);
		
		BundleEvent mockedEvent = mock(BundleEvent.class);
		when(mockedEvent.getBundle()).thenReturn(mockedBundle);
		when(mockedEvent.getType()).thenReturn(BundleEvent.UNINSTALLED);
		
		extender.bundleChanged(mockedEvent);
		
		assertEquals(0, extender.getBundles().size());
		ExtendedBundle bundle = extender.getBundles().peek();
		assertNull(bundle);
	}

	
}
