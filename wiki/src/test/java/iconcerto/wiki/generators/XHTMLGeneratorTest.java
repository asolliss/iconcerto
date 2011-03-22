package iconcerto.wiki.generators;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import iconcerto.wiki.generator.XHTMLGenerator;
import iconcerto.wiki.parser.Cell;
import iconcerto.wiki.parser.Element;
import iconcerto.wiki.parser.Header;
import iconcerto.wiki.parser.Link;
import iconcerto.wiki.parser.Paragraph;
import iconcerto.wiki.parser.ParserVisitor;
import iconcerto.wiki.parser.Row;
import iconcerto.wiki.parser.Span;
import iconcerto.wiki.parser.Span.Type;
import iconcerto.wiki.parser.Table;

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
	public void testParagraphWithItalicSpan() {
		String text = "Simple .";
		
		Span mockedSpan = mock(Span.class);
		when(mockedSpan.getLevel()).thenReturn(2);
		when(mockedSpan.getText()).thenReturn("text");
		when(mockedSpan.getType()).thenReturn(Type.ITALIC);
		when(mockedSpan.getRelativePosition()).thenReturn(7);
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				ParserVisitor visitor = (ParserVisitor) invocation.getArguments()[0];
				visitor.visit((Span) invocation.getMock());
				return null;
			}
		}).when(mockedSpan).accept(any(ParserVisitor.class));
		
		Paragraph mockedParagraph = mock(Paragraph.class);
		when(mockedParagraph.getLevel()).thenReturn(1);
		when(mockedParagraph.getText()).thenReturn(text);		
		when(mockedParagraph.getChildren()).thenReturn(new ArrayList<Element>());
		List<Element> children = new ArrayList<Element>();
		children.add(mockedSpan);		
		when(mockedParagraph.getChildren()).thenReturn(children);	
		
		xhtmlGenerator.visit(mockedParagraph);
		
		assertEquals(
				"<p>Simple <span class=\"italic\">text</span>.</p>", 
				xhtmlGenerator.getDocument()
				);
		
	}
	
	@Test
	public void testParagraphWithItalicSpanIncludesBoldSpan() {
		String text = "Simple .";
		
		Span mockedItalicSpan = mock(Span.class);
		when(mockedItalicSpan.getLevel()).thenReturn(2);
		when(mockedItalicSpan.getText()).thenReturn("italic  text");
		when(mockedItalicSpan.getType()).thenReturn(Type.ITALIC);
		when(mockedItalicSpan.getRelativePosition()).thenReturn(7);
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				ParserVisitor visitor = (ParserVisitor) invocation.getArguments()[0];
				visitor.visit((Span) invocation.getMock());
				return null;
			}
		}).when(mockedItalicSpan).accept(any(ParserVisitor.class));
		
		Span mockedBoldSpan = mock(Span.class);
		when(mockedBoldSpan.getLevel()).thenReturn(3);
		when(mockedBoldSpan.getText()).thenReturn("bold");
		when(mockedBoldSpan.getType()).thenReturn(Type.BOLD);
		when(mockedBoldSpan.getRelativePosition()).thenReturn(7);
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				ParserVisitor visitor = (ParserVisitor) invocation.getArguments()[0];
				visitor.visit((Span) invocation.getMock());
				return null;
			}
		}).when(mockedBoldSpan).accept(any(ParserVisitor.class));
		List<Element> spanChildren = new ArrayList<Element>();
		spanChildren.add(mockedBoldSpan);
		when(mockedItalicSpan.getChildren()).thenReturn(spanChildren);
		
		Paragraph mockedParagraph = mock(Paragraph.class);
		when(mockedParagraph.getLevel()).thenReturn(1);
		when(mockedParagraph.getText()).thenReturn(text);		
		when(mockedParagraph.getChildren()).thenReturn(new ArrayList<Element>());
		List<Element> children = new ArrayList<Element>();
		children.add(mockedItalicSpan);		
		when(mockedParagraph.getChildren()).thenReturn(children);	
		
		xhtmlGenerator.visit(mockedParagraph);
		
		assertEquals(
				"<p>Simple <span class=\"italic\">italic <span class=\"bold\">bold</span> text</span>.</p>", 
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testOneRowTable() {
		String firstCellText = "first cell";
		
		Paragraph mockedParagraph = mock(Paragraph.class);
		when(mockedParagraph.getText()).thenReturn(firstCellText);
		when(mockedParagraph.getChildren()).thenReturn((List)Collections.emptyList());
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				ParserVisitor visitor = (ParserVisitor) invocation.getArguments()[0];
				visitor.visit((Paragraph) invocation.getMock());
				return null;
			}
		}).when(mockedParagraph).accept(any(ParserVisitor.class));
		
		Cell mockedCell = mock(Cell.class);
		List<Element> cellElements = new ArrayList<Element>();
		cellElements.add(mockedParagraph);
		when(mockedCell.getChildren()).thenReturn(cellElements);
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				ParserVisitor visitor = (ParserVisitor) invocation.getArguments()[0];
				visitor.visit((Cell) invocation.getMock());
				return null;
			}
		}).when(mockedCell).accept(any(ParserVisitor.class));
		
		Row mockedRow = mock(Row.class);
		List<Element> rowElements = new ArrayList<Element>();
		rowElements.add(mockedCell);
		when(mockedRow.getChildren()).thenReturn(rowElements);
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				ParserVisitor visitor = (ParserVisitor) invocation.getArguments()[0];
				visitor.visit((Row) invocation.getMock());
				return null;
			}
		}).when(mockedRow).accept(any(ParserVisitor.class));
		
		Table mockedTable = mock(Table.class);
		List<Element> tableElements = new ArrayList<Element>();
		tableElements.add(mockedRow);
		when(mockedTable.getChildren()).thenReturn(tableElements);
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				ParserVisitor visitor = (ParserVisitor) invocation.getArguments()[0];
				visitor.visit((Table) invocation.getMock());
				return null;
			}
		}).when(mockedTable).accept(any(ParserVisitor.class));
		
		xhtmlGenerator.visit(mockedTable);
		
		assertEquals(
				"<table><tr><td><p>"+firstCellText+"</p></td></tr></table>", 
				xhtmlGenerator.getDocument()
				);
	}
	
}
