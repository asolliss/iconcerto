package iconcerto.wiki.parser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;

public class ParserTest {

	private Parser parser;
	
	@Before
	public void setUp() {
		parser = new Parser();
	}
	
	@Test
	public void testParseHeaderAdnLink() {
		String code = "= Header1 =	\n		[[http://www.test.com/|test]]";
		
		ParserVisitors mockedParserVisitor = mock(ParserVisitors.class);
		
		parser.addVisitor(mockedParserVisitor);
		
		parser.parse(code);
		
		verify(mockedParserVisitor).visit(any(Headers.class));
		verify(mockedParserVisitor).visit(any(Links.class));
	}
	
}
