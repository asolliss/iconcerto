package iconcerto.wiki.parser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import iconcerto.wiki.parser.CharAccessor;
import iconcerto.wiki.parser.Element;
import iconcerto.wiki.parser.HeaderParser;
import iconcerto.wiki.parser.Header;
import iconcerto.wiki.parser.ParseBundle;
import iconcerto.wiki.parser.exceptions.ParserException;

import org.junit.*;

public class HeaderParserTest {

	private HeaderParser headerParser;
	
	@Before
	public void setUp() {
		headerParser = new HeaderParser();
	}
	
	@Test
	public void testSimpleFirstLevelHeaderOneLine() {
		String code = "= Header =";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		Element element = headerParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertEquals(Header.class, element.getClass());
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());
	}
	
	@Test(expected=ParserException.class)
	public void testIncorrectHeaderTwoLines() {
		String code = "= Header \n= Header =";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		headerParser.parse(mockedParseBundle);
	}
	
	@Test
	public void testSimpleFirstLevelHeaderTwoLines() {
		String code = "= Header =\n= Header =";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		Element element = headerParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertEquals(Header.class, element.getClass());
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(9, element.getLastCharIndex());
	}
	
	@Test
	public void testSimpleFirstLevelHeaderAtTheMiddleOfLine() {
		String code = " = Header =\n= Header =";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		charAccessor.getChar();
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		Element element = headerParser.parse(mockedParseBundle);
		
		assertNull(element);
	}
	
	@Test
	public void testHeaderLevelAndText() {
		String[] code = {
				"= Header =", "== Header ==", "=== Header ===",
				"==== Header ====", "===== Header =====", "====== Header ======",
				};
		String headerString = " Header ";
		
		int typeNumber = 1;
		for (String snippet: code) {
			ParseBundle mockedParseBundle = mock(ParseBundle.class); 
			CharAccessor charAccessor = new CharAccessor(snippet);
			when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
			
			Element element = headerParser.parse(mockedParseBundle);
			
			assertNotNull(element);
			assertEquals(Header.class, element.getClass());
			Header header = (Header) element;
			assertEquals(headerString, header.getText());
			assertEquals(Header.intToType(typeNumber++), header.getType());
		}
	}
	
}
