package iconcerto.hibernate.extender;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Dictionary;
import java.util.Hashtable;

import iconcerto.extender.ExtendedBundle.Actions;

import org.junit.Test;
import org.osgi.framework.Bundle;

public class HibernateBundleTests {

	@Test
	public void validatingForExtendingWithExtendedBundle() {
		Bundle mockedBundle = mock(Bundle.class);
		Dictionary<String, String> headers = new Hashtable<String, String>();
		headers.put("Bundle-Name", "Test Bundle");
		headers.put(HibernateBundle.ENTITY_CLASSES_HEADER, "iconcerto.data.Entity1, iconcerto.data.Entity2");		
		when(mockedBundle.getHeaders()).thenReturn(headers);
		
		HibernateBundle hibernateBundle = 
				new HibernateBundle(mockedBundle, Actions.ADDING);
		
		assertTrue(hibernateBundle.isValidForExtending());		
	}
	
	@Test
	public void validatingForExtendingWithoutExtendedBundle() {
		Bundle mockedBundle = mock(Bundle.class);
		Dictionary<String, String> headers = new Hashtable<String, String>();
		headers.put("Bundle-Name", "Test Bundle");
		when(mockedBundle.getHeaders()).thenReturn(headers);
		
		HibernateBundle hibernateBundle = 
				new HibernateBundle(mockedBundle, Actions.ADDING);
		
		assertFalse(hibernateBundle.isValidForExtending());		
	}
	
}
