package iconcerto.extender;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class ExtenderClassLoaderTests {

	private ExtenderClassLoader extenderClassLoader;
	
	@Before
	public void setUp() {
		extenderClassLoader = new ExtenderClassLoader();
	}
	
	@Test(expected=ClassNotFoundException.class)
	public void classLoadingWithoutBundlesAndWithoutDefaultClassLoader() throws ClassNotFoundException {		
		extenderClassLoader.loadClass("iconcerto.test.Class");		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void classLoadingWithOneBundleWithNecessaryClassAndWithoutDefaultClassLoader() throws ClassNotFoundException {
		String className = "iconcerto.test.Class";
		
		ExtendedBundle mockedExtendedBundle = mock(ExtendedBundle.class);
		when(mockedExtendedBundle.loadClass(className)).thenReturn((Class) TestClass.class);
		
		extenderClassLoader.addBundle(mockedExtendedBundle);
		
		Class<?> resultClass = null;
		
		try {
			resultClass = extenderClassLoader.loadClass(className);			
		}
		catch (ClassNotFoundException e) {
			assertNull(e);
		}
		
		assertNotNull(resultClass);
		assertSame(TestClass.class, resultClass);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void classLoadingWithOneBundleWithNecessaryClassAndDefaultClassLoader() throws ClassNotFoundException {
		String className = "iconcerto.test.Class";
		
		ExtendedBundle mockedExtendedBundle = mock(ExtendedBundle.class);
		when(mockedExtendedBundle.loadClass(className)).thenReturn((Class) TestClass.class);
		ClassLoader mockedDefaultClassLoader = mock(ClassLoader.class);		
		
		extenderClassLoader.setDefaultClassLoader(mockedDefaultClassLoader);
		extenderClassLoader.addBundle(mockedExtendedBundle);		
		
		Class<?> resultClass = null;
		
		try {
			resultClass = extenderClassLoader.loadClass(className);			
		}
		catch (ClassNotFoundException e) {
			assertNull(e);
		}
		
		verify(mockedDefaultClassLoader, never()).loadClass(anyString());
		
		assertNotNull(resultClass);
		assertSame(TestClass.class, resultClass);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void classLoadingWithOneBundleAndDefaultClassLoaderWithNecessaryClass() throws ClassNotFoundException {
		String className = "iconcerto.test.Class";
		
		ExtendedBundle mockedExtendedBundle = mock(ExtendedBundle.class);
		when(mockedExtendedBundle.loadClass(anyString())).thenThrow(new ClassNotFoundException());
		ClassLoader mockedDefaultClassLoader = mock(ClassLoader.class);
		when(mockedDefaultClassLoader.loadClass(className)).thenReturn((Class) TestClass.class);
		
		extenderClassLoader.setDefaultClassLoader(mockedDefaultClassLoader);
		extenderClassLoader.addBundle(mockedExtendedBundle);		
		
		Class<?> resultClass = null;
		
		try {
			resultClass = extenderClassLoader.loadClass(className);			
		}
		catch (ClassNotFoundException e) {
			assertNull(e);
		}
		
		verify(mockedDefaultClassLoader, only()).loadClass(anyString());
		
		assertNotNull(resultClass);
		assertSame(TestClass.class, resultClass);
		
	}
	
	private static class TestClass {	
	}
}
