package iconcerto.extender;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
	
	private static class TestClass {	
	}
}
