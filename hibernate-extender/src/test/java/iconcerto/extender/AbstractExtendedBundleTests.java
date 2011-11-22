package iconcerto.extender;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import iconcerto.extender.ExtendedBundle.Actions;

import org.junit.Test;

import org.osgi.framework.Bundle;

public class AbstractExtendedBundleTests {
	
	public ExtendedBundle createExtendedBundle(Bundle bundle, Actions action) {
		return new AbstractExtendedBundle(bundle, action) {			
			@Override
			public boolean isValidForExtending() {
				return false;
			}

			@Override
			public void extend() {
				
			}

			@Override
			protected boolean isValidNameForLoading(String name) {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}
	
	@Test
	public void generatingHashCode() {
		long bundle1Id = 1000L;
		long bundle2Id = 1001L;
		
		Bundle mockedBundle1 = mock(Bundle.class);
		when(mockedBundle1.getBundleId()).thenReturn(bundle1Id);
		
		Bundle mockedBundle2 = mock(Bundle.class);
		when(mockedBundle2.getBundleId()).thenReturn(bundle2Id);
		
		ExtendedBundle extendedBundle1 = createExtendedBundle(mockedBundle1, Actions.ADDING);
		
		ExtendedBundle extendedBundle2 = createExtendedBundle(mockedBundle2, Actions.ADDING);
		
		assertEquals(extendedBundle1.hashCode(), extendedBundle1.hashCode());
		assertEquals(extendedBundle2.hashCode(), extendedBundle2.hashCode());
		assertNotSame(extendedBundle1.hashCode(), extendedBundle2.hashCode());
		
	}
	
	@Test
	public void equalBundles() {
		long bundle1Id = 1000L;
		long bundle2Id = 1000L;
		
		Bundle mockedBundle1 = mock(Bundle.class);
		when(mockedBundle1.getBundleId()).thenReturn(bundle1Id);
		
		Bundle mockedBundle2 = mock(Bundle.class);
		when(mockedBundle2.getBundleId()).thenReturn(bundle2Id);
		
		ExtendedBundle extendedBundle1 = createExtendedBundle(mockedBundle1, Actions.ADDING);
		
		ExtendedBundle extendedBundle2 = createExtendedBundle(mockedBundle2, Actions.ADDING);
		
		assertTrue(extendedBundle1.equals(extendedBundle2));
		assertTrue(extendedBundle2.equals(extendedBundle1));
		
	}
	
	@Test
	public void unequalBundles() {
		long bundle1Id = 1000L;
		long bundle2Id = 1001L;
		
		Bundle mockedBundle1 = mock(Bundle.class);
		when(mockedBundle1.getBundleId()).thenReturn(bundle1Id);
		
		Bundle mockedBundle2 = mock(Bundle.class);
		when(mockedBundle2.getBundleId()).thenReturn(bundle2Id);
		
		ExtendedBundle extendedBundle1 = createExtendedBundle(mockedBundle1, Actions.ADDING);
		
		ExtendedBundle extendedBundle2 = createExtendedBundle(mockedBundle2, Actions.ADDING);
		
		assertFalse(extendedBundle1.equals(extendedBundle2));
		assertFalse(extendedBundle2.equals(extendedBundle1));
		
	}
	
}
