package iconcerto.wiki.parser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;

public class ParagraphParserTest {

	private ParagraphParser paragraphParser;
	
	@Before
	public void setUp() {
		paragraphParser = new ParagraphParser();
	}
	
	@Test
	public void testSingleLineTextParsing() {
		String code = " Simple text ";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		paragraphParser.setCurrentParent(mock(Element.class));
		Element element = paragraphParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());
		assertNotNull(element.getParent());
		assertEquals(Paragraph.class, element.getClass());		
		Paragraph paragraph = (Paragraph) element;
		assertEquals(code, paragraph.getText());	
	}
	
	@Test
	public void testTwoLinesTextParsing() {
		String code = " Simple text\n" +
				"Simple text";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		paragraphParser.setCurrentParent(mock(Element.class));
		Element element = paragraphParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(11, element.getLastCharIndex());
		assertNotNull(element.getParent());
		assertEquals(Paragraph.class, element.getClass());		
		Paragraph paragraph = (Paragraph) element;
		assertEquals(" Simple text", paragraph.getText());
		assertEquals(13, charAccessor.getIndex());		
	}
	
	@Test
	public void testSingleLineWithLinkTextParsing() {
		String code = " Simple text [[Link|Titled Link]].";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		paragraphParser.setCurrentParent(mock(Element.class));
		Element element = paragraphParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());
		assertNotNull(element.getParent());
		assertEquals(Paragraph.class, element.getClass());		
		Paragraph paragraph = (Paragraph) element;
		assertEquals(" Simple text .", paragraph.getText());
		assertEquals(1, paragraph.getChildren().size());
		assertEquals(Link.class, paragraph.getChildren().get(0).getClass());
		assertEquals(13, paragraph.getChildren().get(0).getRelativePosition());
	}
	
	@Test
	public void testSingleLineWithTwoLinksTextParsing() {
		String code = " Simple text [[Link1|Titled Link1]]. Simple text [[Link2|Titled Link2]].";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		paragraphParser.setCurrentParent(mock(Element.class));
		Element element = paragraphParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());
		assertNotNull(element.getParent());
		assertEquals(Paragraph.class, element.getClass());		
		Paragraph paragraph = (Paragraph) element;
		assertEquals(" Simple text . Simple text .", paragraph.getText());
		assertEquals(2, paragraph.getChildren().size());
		assertEquals(Link.class, paragraph.getChildren().get(0).getClass());
		assertEquals(13, paragraph.getChildren().get(0).getRelativePosition());
		assertEquals(Link.class, paragraph.getChildren().get(1).getClass());
		assertEquals(27, paragraph.getChildren().get(1).getRelativePosition());
	}
}
