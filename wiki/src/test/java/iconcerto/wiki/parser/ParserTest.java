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
	public void testBeginningAndEnding() {
		String code = " ";
		
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
	public void testParseHeaderAndLink() {
		String code = "= Header1 =	\n		[[http://www.test.com/|test]]";
		
		ParserVisitor mockedParserVisitor = mock(ParserVisitor.class);
		
		parser.addVisitor(mockedParserVisitor);
		
		parser.parse(code);
		
		verify(mockedParserVisitor).visit(any(Header.class));
		verify(mockedParserVisitor).visit(any(Paragraph.class));
	}
	
}
