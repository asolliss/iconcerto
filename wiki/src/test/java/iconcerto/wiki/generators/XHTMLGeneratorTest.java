package iconcerto.wiki.generators;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import iconcerto.wiki.parser.Headers;
import iconcerto.wiki.parser.Links;

import org.junit.*;

public class XHTMLGeneratorTest {

	private XHTMLGenerator xhtmlGenerator;
	
	@Before
	public void setUp() {
		xhtmlGenerator = new XHTMLGenerator();
	}
	
	@Test
	public void testHeaders() {		
		for (Headers.Types t: Headers.Types.values()) {
			Headers mockedHeader = mock(Headers.class);
			when(mockedHeader.getType()).thenReturn(t);
			when(mockedHeader.getText()).thenReturn("text");
			
			xhtmlGenerator.visit(mockedHeader);
			
			String tagName = t.toString().toLowerCase();
			
			assertEquals(
					"<"+tagName+">text</"+tagName+">\n",
					xhtmlGenerator.getDocument()
					);
			
			xhtmlGenerator.clear();
		}
	}
	
	@Test
	public void testInternalLink() {
		Links mockedLink = mock(Links.class);
		when(mockedLink.getType()).thenReturn(Links.Types.INTERNAL);
		when(mockedLink.getUrl()).thenReturn("Index");
		when(mockedLink.getTitle()).thenReturn("Main page");
		
		xhtmlGenerator.setParameter(XHTMLGenerator.INTERNAL_LINK_PATTERN, "/wiki/{value}");
		
		xhtmlGenerator.visit(mockedLink);
	
		assertEquals(
				"<a href=\"/wiki/Index\">Main page</a>",
				xhtmlGenerator.getDocument()
				);
	}
	
	@Test
	public void testExternalLink() {
		Links mockedLink = mock(Links.class);
		when(mockedLink.getType()).thenReturn(Links.Types.EXTERNAL);
		when(mockedLink.getUrl()).thenReturn("http://www.test.com");
		when(mockedLink.getTitle()).thenReturn("www.test.com");
		
		xhtmlGenerator.visit(mockedLink);
	
		assertEquals(
				"<a href=\"http://www.test.com\">www.test.com</a>",
				xhtmlGenerator.getDocument()
				);
	}
	
}
