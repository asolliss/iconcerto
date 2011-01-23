package iconcerto.wiki.parser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import iconcerto.wiki.parser.LinkParser;

import org.junit.*;

public class LinkParserTest {

	private LinkParser linkParser;
	
	@Before
	public void setUp() {
		linkParser = new LinkParser();
	}
	
	@Test
	public void testLinkParsingInternalLink() {
		String code = "[[Link]]";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		linkParser.setCurrentParent(mock(Element.class));
		Element element = linkParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertNotNull(element.getParent());
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());		
		assertEquals(Link.class, element.getClass());		
		Link link = (Link) element;
		assertEquals(Link.Type.INTERNAL, link.getType());
		assertEquals("Link", link.getUrl());
		assertEquals("Link", link.getTitle());		
	}
	
	@Test
	public void testLinkParsingInternalTitledLink() {
		String code = "[[Link|Titled link]]";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		linkParser.setCurrentParent(mock(Element.class));
		Element element = linkParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertNotNull(element.getParent());
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());
		assertEquals(Link.class, element.getClass());
		Link link = (Link) element;
		assertEquals(Link.Type.INTERNAL, link.getType());
		assertEquals("Link", link.getUrl());
		assertEquals("Titled link", link.getTitle());
	}
	
	@Test
	public void testLinkParsingExternalLink() {
		String code = "[[http://www.test.com/]]";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		linkParser.setCurrentParent(mock(Element.class));
		Element element = linkParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertNotNull(element.getParent());
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());
		assertEquals(Link.class, element.getClass());		
		Link link = (Link) element;
		assertEquals(Link.Type.EXTERNAL, link.getType());
		assertEquals("http://www.test.com/", link.getUrl());
		assertEquals("http://www.test.com/", link.getTitle());		
	}
	
	@Test
	public void testLinkParsingExternalTitledLink() {
		String code = "[[http://www.test.com/|test.com]]";
		ParseBundle mockedParseBundle = mock(ParseBundle.class); 
		CharAccessor charAccessor = new CharAccessor(code);
		when(mockedParseBundle.getCharAccessor()).thenReturn(charAccessor);
		
		linkParser.setCurrentParent(mock(Element.class));
		Element element = linkParser.parse(mockedParseBundle);
		
		assertNotNull(element);
		assertNotNull(element.getParent());
		assertEquals(0, element.getFirstCharIndex());
		assertEquals(code.length()-1, element.getLastCharIndex());
		assertEquals(Link.class, element.getClass());
		Link link = (Link) element;
		assertEquals(Link.Type.EXTERNAL, link.getType());
		assertEquals("http://www.test.com/", link.getUrl());
		assertEquals("test.com", link.getTitle());
	}
	
}
