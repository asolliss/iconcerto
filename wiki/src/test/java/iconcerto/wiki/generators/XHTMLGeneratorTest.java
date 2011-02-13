package iconcerto.wiki.generators;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import iconcerto.wiki.generator.XHTMLGenerator;
import iconcerto.wiki.parser.Element;
import iconcerto.wiki.parser.Header;
import iconcerto.wiki.parser.Link;
import iconcerto.wiki.parser.Paragraph;
import iconcerto.wiki.parser.ParserVisitor;

import org.junit.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class XHTMLGeneratorTest {

	private XHTMLGenerator xhtmlGenerator;
	
	@Before
	public void setUp() {
		xhtmlGenerator = new XHTMLGenerator();
	}
	
	@Test
	public void testHeaders() {		
		for (Header.Type t: Header.Type.values()) {
			Header mockedHeader = mock(Header.class);
			when(mockedHeader.getType()).thenReturn(t);
			when(mockedHeader.getText()).thenReturn("text");
			
			xhtmlGenerator.visit(mockedHeader);
			
			String tagName = t.toString().toLowerCase();
			
			assertEquals(
					"<"+tagName+">text</"+tagName+">",
					xhtmlGenerator.getDocument()
					);
			
			xhtmlGenerator.clear();
		}
	}
	
	@Test
	public void testInternalLink() {
		Link mockedLink = mock(Link.class);
		when(mockedLink.getType()).thenReturn(Link.Type.INTERNAL);
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
		Link mockedLink = mock(Link.class);
		when(mockedLink.getType()).thenReturn(Link.Type.EXTERNAL);
		when(mockedLink.getUrl()).thenReturn("http://www.test.com");
		when(mockedLink.getTitle()).thenReturn("www.test.com");
		
		xhtmlGenerator.visit(mockedLink);
	
		assertEquals(
				"<a href=\"http://www.test.com\">www.test.com</a>",
				xhtmlGenerator.getDocument()
				);
	}
	
	@Test
	public void testSimpleParagraph() {
		String text = "Simple text.";
		Paragraph mockedParagraph = mock(Paragraph.class);
		when(mockedParagraph.getLevel()).thenReturn(1);
		when(mockedParagraph.getText()).thenReturn(text);		
		when(mockedParagraph.getChildren()).thenReturn(new ArrayList<Element>());
		
		xhtmlGenerator.visit(mockedParagraph);
		
		assertEquals(
				"<p>"+text+"</p>", 
				xhtmlGenerator.getDocument()
				);
		
	}
	
	@Test
	public void testParagraphWithLink() {
		String text = "Simple  text.";
		
		Link mockedLink = mock(Link.class);
		when(mockedLink.getLevel()).thenReturn(2);
		when(mockedLink.getTitle()).thenReturn("link1");
		when(mockedLink.getType()).thenReturn(Link.Type.EXTERNAL);
		when(mockedLink.getUrl()).thenReturn("http://test.com");
		when(mockedLink.getRelativePosition()).thenReturn(7);
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				ParserVisitor visitor = (ParserVisitor) invocation.getArguments()[0];
				visitor.visit((Link) invocation.getMock());
				return null;
			}
		}).when(mockedLink).accept(any(ParserVisitor.class));
		
		Paragraph mockedParagraph = mock(Paragraph.class);
		when(mockedParagraph.getLevel()).thenReturn(1);
		when(mockedParagraph.getText()).thenReturn(text);		
		List<Element> children = new ArrayList<Element>();
		children.add(mockedLink);		
		when(mockedParagraph.getChildren()).thenReturn(children);		
		
		xhtmlGenerator.visit(mockedParagraph);
		
		assertEquals(
				"<p>Simple <a href=\"http://test.com\">link1</a> text.</p>", 
				xhtmlGenerator.getDocument()
				);
		
	}
	
	@Test
	public void testParagraphWithLinkAtTheEnd() {
		String text = "Simple  text ";
		
		Link mockedLink = mock(Link.class);
		when(mockedLink.getLevel()).thenReturn(2);
		when(mockedLink.getTitle()).thenReturn("link1");
		when(mockedLink.getType()).thenReturn(Link.Type.EXTERNAL);
		when(mockedLink.getUrl()).thenReturn("http://test.com");
		when(mockedLink.getRelativePosition()).thenReturn(13);
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				ParserVisitor visitor = (ParserVisitor) invocation.getArguments()[0];
				visitor.visit((Link) invocation.getMock());
				return null;
			}
		}).when(mockedLink).accept(any(ParserVisitor.class));
		
		Paragraph mockedParagraph = mock(Paragraph.class);
		when(mockedParagraph.getLevel()).thenReturn(1);
		when(mockedParagraph.getText()).thenReturn(text);		
		List<Element> children = new ArrayList<Element>();
		children.add(mockedLink);		
		when(mockedParagraph.getChildren()).thenReturn(children);		
		
		xhtmlGenerator.visit(mockedParagraph);
		
		assertEquals(
				"<p>Simple  text <a href=\"http://test.com\">link1</a></p>", 
				xhtmlGenerator.getDocument()
				);
		
	}
	
	@Test
	public void testParagraphWithTwoLinks() {
		String text = "Simple  text .";
		
		Link mockedLink1 = mock(Link.class);
		when(mockedLink1.getLevel()).thenReturn(2);
		when(mockedLink1.getTitle()).thenReturn("link1");
		when(mockedLink1.getType()).thenReturn(Link.Type.EXTERNAL);
		when(mockedLink1.getUrl()).thenReturn("http://test.com");
		when(mockedLink1.getRelativePosition()).thenReturn(7);
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				ParserVisitor visitor = (ParserVisitor) invocation.getArguments()[0];
				visitor.visit((Link) invocation.getMock());
				return null;
			}
		}).when(mockedLink1).accept(any(ParserVisitor.class));
		
		Link mockedLink2 = mock(Link.class);
		when(mockedLink2.getLevel()).thenReturn(2);
		when(mockedLink2.getTitle()).thenReturn("link2");
		when(mockedLink2.getType()).thenReturn(Link.Type.EXTERNAL);
		when(mockedLink2.getUrl()).thenReturn("http://test.com");
		when(mockedLink2.getRelativePosition()).thenReturn(13);	
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				ParserVisitor visitor = (ParserVisitor) invocation.getArguments()[0];
				visitor.visit((Link) invocation.getMock());
				return null;
			}
		}).when(mockedLink2).accept(any(ParserVisitor.class));
		
		Paragraph mockedParagraph = mock(Paragraph.class);
		when(mockedParagraph.getLevel()).thenReturn(1);
		when(mockedParagraph.getText()).thenReturn(text);		
		List<Element> children = new ArrayList<Element>();
		children.add(mockedLink1);
		children.add(mockedLink2);
		when(mockedParagraph.getChildren()).thenReturn(children);		
		
		xhtmlGenerator.visit(mockedParagraph);
		
		assertEquals(
				"<p>Simple <a href=\"http://test.com\">link1</a> text <a href=\"http://test.com\">link2</a>.</p>", 
				xhtmlGenerator.getDocument()
				);
		
	}
	
}
