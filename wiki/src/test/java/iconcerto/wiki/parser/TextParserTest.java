package iconcerto.wiki.parser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;

public class TextParserTest {

	private TextParser textParser;
	
	@Before
	public void setUp() {
		textParser = new TextParser();
	}
	
	@Test
	public void testSingleLineTextParsing() {
		String code = " Simple text ";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		textParser.setCurrentParent(new Root());
		
		Elements element = textParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());
		assertNotNull(element.getParent());
		assertEquals(Root.class, element.getParent().getClass());
		assertEquals(Texts.class, element.getClass());		
		Texts text = (Texts) element;
		assertEquals(code, text.getText());	
	}
	
	@Test
	public void testTwoLinesTextParsing() {
		String code = " Simple text\n" +
				"Simple text";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		textParser.setCurrentParent(new Root());
		
		Elements element = textParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(11, element.getLastCharIndex());
		assertNotNull(element.getParent());
		assertEquals(Root.class, element.getParent().getClass());
		assertEquals(Texts.class, element.getClass());		
		Texts text = (Texts) element;
		assertEquals(" Simple text", text.getText());	
	}
	
	@Test
	public void testSingleLineWithLinkTextParsing() {
		String code = " Simple text [[Link|Titled Link]].";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		textParser.setCurrentParent(new Root());
		
		Elements element = textParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());
		assertNotNull(element.getParent());
		assertEquals(Root.class, element.getParent().getClass());
		assertEquals(Texts.class, element.getClass());		
		Texts text = (Texts) element;
		assertEquals(" Simple text .", text.getText());
		assertEquals(1, text.getChildren().size());
		assertEquals(Links.class, text.getChildren().get(0).getClass());
		assertEquals(13, text.getPosition(text.getChildren().get(0)));
	}
	
	@Test
	public void testSingleLineWithTwoLinksTextParsing() {
		String code = " Simple text [[Link1|Titled Link1]]. Simple text [[Link2|Titled Link2]].";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		textParser.setCurrentParent(new Root());
		
		Elements element = textParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());
		assertNotNull(element.getParent());
		assertEquals(Root.class, element.getParent().getClass());
		assertEquals(Texts.class, element.getClass());		
		Texts text = (Texts) element;
		assertEquals(" Simple text . Simple text .", text.getText());
		assertEquals(2, text.getChildren().size());
		assertEquals(Links.class, text.getChildren().get(0).getClass());
		assertEquals(13, text.getPosition(text.getChildren().get(0)));
		assertEquals(Links.class, text.getChildren().get(1).getClass());
		assertEquals(27, text.getPosition(text.getChildren().get(1)));
	}
}
