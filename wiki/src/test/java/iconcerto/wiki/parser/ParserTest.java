package iconcerto.wiki.parser;

import static org.junit.Assert.*;
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
		
		ParserVisitors mockedParserVisitor = mock(ParserVisitors.class);
		
		parser.addVisitor(mockedParserVisitor);
		
		parser.parse(code);
		
		InOrder inOrder = inOrder(mockedParserVisitor);
		
		inOrder.verify(mockedParserVisitor).begin();
		inOrder.verify(mockedParserVisitor).visit(any(Texts.class));
		inOrder.verify(mockedParserVisitor).end();
		verifyNoMoreInteractions(mockedParserVisitor);
	}
	
	@Test
	public void testParseHeaderAndLink() {
		String code = "= Header1 =	\n		[[http://www.test.com/|test]]";
		
		ParserVisitors mockedParserVisitor = mock(ParserVisitors.class);
		
		parser.addVisitor(mockedParserVisitor);
		
		parser.parse(code);
		
		verify(mockedParserVisitor).visit(any(Headers.class));
		verify(mockedParserVisitor).visit(any(Texts.class));
	}
	
}
