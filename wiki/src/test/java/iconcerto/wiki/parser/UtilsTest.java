package iconcerto.wiki.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UtilsTest {

	private CharAccessor charAccessor;
	private String testString;
	
	@Before
	public void setUp() {
		testString = "test string ttt\neee abc";
		charAccessor = new CharAccessor(testString);		
	}
	
	@Test
	public void testMatch() {
		char[] sequence = "te".toCharArray();
		
		assertTrue(Utils.match(sequence, charAccessor));		
	}
	
	@Test
	public void testMismatch() {
		char[] sequence = "st".toCharArray();
		
		assertFalse(Utils.match(sequence, charAccessor));		
	}
	
}
