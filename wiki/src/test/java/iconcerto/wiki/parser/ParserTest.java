package iconcerto.wiki.parser;

import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.InOrder;

public class ParserTest {

	private Parser parser;
	
	@Before
	public void setUp() {
		parser = new Parser();
	}
	
	@Test
	public void testVisitOnlyRootElements() {
		Element mockedElement = mock(Element.class);
		Element parent = mock(Element.class);
		when(mockedElement.getParent()).thenReturn(parent);
		
		ParserVisitor mockedParserVisitor = mock(ParserVisitor.class);
		when(mockedParserVisitor.isOnlyRoot()).thenReturn(true);
		
		parser.addVisitor(mockedParserVisitor);
		
		verify(mockedElement, never()).accept(any(ParserVisitor.class));
	}
	
	@Test
	public void testParseSimpleParagraph() {
		String code = "It's a simple paragraph. ";
		
		ParserVisitor mockedParserVisitor = mock(ParserVisitor.class);
		when(mockedParserVisitor.isOnlyRoot()).thenReturn(false);
		
		parser.addVisitor(mockedParserVisitor);
		
		parser.parse(code);
		
		InOrder inOrder = inOrder(mockedParserVisitor);
				
		inOrder.verify(mockedParserVisitor, atLeastOnce()).isOnlyRoot();
		inOrder.verify(mockedParserVisitor).visit(any(Paragraph.class));
		verifyNoMoreInteractions(mockedParserVisitor);
	}
	
	@Test
	public void testParsetwoSimpleParagraphs() {
		String code = "It's the first simple paragraph.\nIt's the second simple paragraph.";
		
		ParserVisitor mockedParserVisitor = mock(ParserVisitor.class);
		when(mockedParserVisitor.isOnlyRoot()).thenReturn(false);
		
		parser.addVisitor(mockedParserVisitor);
		
		parser.parse(code);
		
		InOrder inOrder = inOrder(mockedParserVisitor);
				
		inOrder.verify(mockedParserVisitor).isOnlyRoot();
		inOrder.verify(mockedParserVisitor).visit(any(Paragraph.class));
		inOrder.verify(mockedParserVisitor).isOnlyRoot();
		inOrder.verify(mockedParserVisitor).visit(any(Paragraph.class));
		verifyNoMoreInteractions(mockedParserVisitor);
	}
	
	@Test
	public void testParseHeaderAndLink() {
		String code = "= Header1 =	\n		[[http://www.test.com/|test]]";
		
		ParserVisitor mockedParserVisitor = mock(ParserVisitor.class);
		
		parser.addVisitor(mockedParserVisitor);
		
		parser.parse(code);
		
		InOrder inOrder = inOrder(mockedParserVisitor);
		
		inOrder.verify(mockedParserVisitor).isOnlyRoot();
		inOrder.verify(mockedParserVisitor).visit(any(Header.class));
		inOrder.verify(mockedParserVisitor).isOnlyRoot();
		inOrder.verify(mockedParserVisitor).visit(any(Paragraph.class));
		inOrder.verify(mockedParserVisitor).isOnlyRoot();
		inOrder.verify(mockedParserVisitor).visit(any(Link.class));
		verifyNoMoreInteractions(mockedParserVisitor);
	}
	
}
