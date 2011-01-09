package iconcerto.wiki.parser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import iconcerto.wiki.parser.CharAccessor;
import iconcerto.wiki.parser.Elements;
import iconcerto.wiki.parser.HeaderParser;
import iconcerto.wiki.parser.Headers;
import iconcerto.wiki.parser.ParseBundle;

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
		
		Elements element = headerParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertEquals(Headers.class, element.getClass());
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());
	}
	
	@Test(expected=ParserRuntimeException.class)
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
		
		headerParser.setCurrentParent(new Root());
		Elements element = headerParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertEquals(Headers.class, element.getClass());
		assertNotNull(element.getParent());
		assertEquals(Root.class, element.getParent().getClass());
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
		
		Elements element = headerParser.parse(mockedParseBundle);
		
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
			
			headerParser.setCurrentParent(new Root());
			Elements element = headerParser.parse(mockedParseBundle);
			
			assertNotNull(element);
			assertEquals(Headers.class, element.getClass());
			assertNotNull(element.getParent());
			assertEquals(Root.class, element.getParent().getClass());
			Headers header = (Headers) element;
			assertEquals(headerString, header.getText());
			assertEquals(Headers.intToType(typeNumber++), header.getType());
		}
	}
	
}
