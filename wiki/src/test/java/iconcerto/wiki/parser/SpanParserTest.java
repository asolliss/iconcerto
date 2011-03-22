package iconcerto.wiki.parser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import iconcerto.wiki.parser.Span.Type;

import org.junit.*;

public class SpanParserTest {

	private SpanParser spanParser;
	
	@Before
	public void setUp() {
		spanParser = new SpanParser();
	}
	
	@Test
	public void testSpanParsingItalic() {
		String code = "''span''";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		spanParser.setCurrentParent(mock(Element.class));
		Element element = spanParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertNotNull(element.getParent());
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());		
		assertEquals(Span.class, element.getClass());		
		Span span = (Span) element;
		assertEquals(Type.ITALIC, span.getType());
		assertEquals("span", span.getText());		
	}
	
	@Test
	public void testSpanParsingItalicWithInsertedBold() {
		String code = "''\"\"bold\"\" italic''";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		spanParser.setCurrentParent(mock(Element.class));
		Element element = spanParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertNotNull(element.getParent());
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());		
		assertEquals(Span.class, element.getClass());		
		Span span = (Span) element;
		assertEquals(Type.ITALIC, span.getType());
		assertEquals(" italic", span.getText());	
		
		//for child
		assertEquals(1, element.getChildren().size());
		assertNotNull(element.getChildren().get(0));
		assertEquals(Span.class, element.getChildren().get(0).getClass());
		Span child = (Span) element.getChildren().get(0);
		assertEquals(Type.BOLD, child.getType());
		assertEquals("bold", child.getText());
	}
	
	/*@Test(expected=ParserRuntimeException.class)
	public void testSpanParsingItalicIncorrectText() {
		String code = "''sp\nan''";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		spanParser.setCurrentParent(mock(Element.class));
		Element element = spanParser.parse(mockedParseBundle);		
	}*/
	
}
