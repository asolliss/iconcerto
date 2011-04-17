package iconcerto.wiki.parser;

import static org.junit.Assert.*;
import iconcerto.wiki.parser.CharAccessor;
import iconcerto.wiki.parser.exceptions.ConfigurationException;
import iconcerto.wiki.parser.exceptions.ParserException;
import iconcerto.wiki.parser.exceptions.ParsingException;

import org.junit.*;

public class CharAccessorTest {

	private CharAccessor charAccessor;
	private String testString;
	
	@Before
	public void setUp() {
		testString = "test string ttt\neee abc";
		charAccessor = new CharAccessor(testString);		
	}
	
	@Test
	public void testFirstCharGetting() {
		int firstCharIndex = 0;
		assertEquals(testString.charAt(firstCharIndex), charAccessor.getChar());
	}
	
	@Test
	public void testThirdCharGetting() {
		int thirdCharIndex = 2;
		charAccessor.setIndex(thirdCharIndex);
		assertEquals(testString.charAt(thirdCharIndex), charAccessor.getChar());
	}
	
	@Test
	public void testIndexGetting() {
		assertEquals(0, charAccessor.getIndex());
		charAccessor.setIndex(1);
		assertEquals(1, charAccessor.getIndex());
	}
	
	@Test
	public void testPreviousIndexGetting() {
		charAccessor.setIndex(1);
		assertEquals(0, charAccessor.getPreviousIndex());
		charAccessor.setIndex(2);
		assertEquals(1, charAccessor.getPreviousIndex());
	}
	
	@Test
	public void testCharReturning() {
		charAccessor.setIndex(1);
		charAccessor.returnChar();
		assertEquals(0, charAccessor.getIndex());
	}
	
	@Test
	public void testRangeGetting() {
		int beginning = 3;
		int end = 6;
		assertArrayEquals(
				testString.substring(beginning, end).toCharArray(),
				charAccessor.getRange(beginning, end)
				);
	}
	
	@Test
	public void testSkipPreviousCharRepetitionInTheEndOfString() {
		testString = "abc aaa";
		charAccessor.init(testString);
		charAccessor.setIndex(5);
		
		assertEquals(2, charAccessor.skipPreviousCharRepetition());
		assertEquals(7, charAccessor.getIndex());
	}
	
	@Test
	public void testSkipPreviousCharRepetitionInTheMiddleOfString() {
		testString = "abc aaa def";
		charAccessor.init(testString);
		charAccessor.setIndex(5);
		
		assertEquals(2, charAccessor.skipPreviousCharRepetition());
		assertEquals(7, charAccessor.getIndex());
	}
	
	@Test
	public void testSkipPreviousCharRepetitionInTheEndOfLine() {
		testString = "abc aaa\n" +
				"yeah!";
		charAccessor.init(testString);
		charAccessor.setIndex(5);
		
		assertEquals(2, charAccessor.skipPreviousCharRepetition());
		assertEquals(7, charAccessor.getIndex());
	}
	
	@Test(expected=ParsingException.class)
	public void testSearchWithNullArgument() {
		charAccessor.lookFor(null);
	}
	
	@Test(expected=ParsingException.class)
	public void testSearchWithEmptyArgument() {
		charAccessor.lookFor(new char[0]);
	}
	
	@Test(expected=ParsingException.class)
	public void testSearchWithNonexistentArgument() {
		char chars[] = "strign1".toCharArray();
		charAccessor.lookFor(chars);
	}
	
	@Test(expected=ParsingException.class)
	public void testSearchWithNonexistentSingleCharArgument() {
		char chars[] = "z".toCharArray();
		charAccessor.lookForAtSingleLine(chars);
	}
	
	@Test
	public void testSearch() {
		char chars[] = "string".toCharArray();
		charAccessor.lookFor(chars);
		
		assertEquals(11, charAccessor.getIndex());
	}
	
	@Test(expected=ParserException.class)
	public void testSingleLineSearchWithNullArgument() {
		charAccessor.lookForAtSingleLine(null);
	}
	
	@Test(expected=ParsingException.class)
	public void testSingleLineSearchWithEmptyArgument() {
		charAccessor.lookForAtSingleLine(new char[0]);
	}
	
	@Test(expected=ParsingException.class)
	public void testSingleLineSearchWithNonexistentArgument() {
		char chars[] = "string1".toCharArray();
		charAccessor.lookForAtSingleLine(chars);
	}
	
	@Test(expected=ParsingException.class)
	public void testSingleLineSearchWithNonexistentSingleCharArgument() {
		char chars[] = "a".toCharArray();
		charAccessor.lookForAtSingleLine(chars);
	}
	
	@Test
	public void testSingleLineSearch() {
		char chars[] = "string".toCharArray();
		charAccessor.lookForAtSingleLine(chars);
		
		assertEquals(11, charAccessor.getIndex());
	}
	
	@Test
	public void testFirstCharOfLine() {
		assertTrue(charAccessor.isFirstCharOfLine());
		charAccessor.getChar();
		assertFalse(charAccessor.isFirstCharOfLine());
	}
	
	@Test
	public void testMultilineFirstCharOfLine() {
		testString = "abc aaa\n" +
					 "yeah!";
		charAccessor.init(testString);
		charAccessor.setIndex(8);
		assertTrue(charAccessor.isFirstCharOfLine());
		charAccessor.getChar();
		assertFalse(charAccessor.isFirstCharOfLine());
	}
	
	@Test
	public void testSkipLine() {
		charAccessor.skipLine();
		assertEquals(16, charAccessor.getIndex());
	}
	
	@Test
	public void testSkipLineAtEndOfCode() {
		charAccessor.setIndex(16);
		charAccessor.skipLine();
		
		assertEquals(23, charAccessor.getIndex());
	}
	
	@Test
	public void testPushOneStopSequence() {
		//"test string ttt\neee abc"
		charAccessor.pushStopSequence("str".toCharArray());
		
		while (charAccessor.hasNext()) {
			charAccessor.getChar();
		}
		
		assertEquals(4, charAccessor.getPreviousIndex());
	}
	
	@Test
	public void testPushAndPopTwoStopSequence() {
		//"test string ttt\neee abc"
		charAccessor.pushStopSequence("str".toCharArray());
		charAccessor.pushStopSequence("st".toCharArray());
		
		while (charAccessor.hasNext()) {
			charAccessor.getChar();
		}
		
		assertEquals(1, charAccessor.getPreviousIndex());
		
		assertArrayEquals("st".toCharArray(), charAccessor.popStopSequence());
		
		while (charAccessor.hasNext()) {
			charAccessor.getChar();
		}
		
		assertEquals(4, charAccessor.getPreviousIndex());
		assertArrayEquals("str".toCharArray(), charAccessor.popStopSequence());
	}
	
	@Test(expected=ConfigurationException.class)
	public void testPopOnEmptyStopSequence() {
		charAccessor.popStopSequence();
	}
	
	@Test
	public void testEmptyTillEndOfLineWithPositiveResult() {
		String testString = "first line   \nsecond line  ";
		charAccessor.init(testString);
		charAccessor.setIndex(10);
		
		assertTrue(charAccessor.isEmptyTillEndOfLine());
		assertEquals(14, charAccessor.getIndex());
	}
	
	@Test
	public void testEmptyTillEndOfLineWithNegativeResult() {
		String testString = "first line\nsecond line  ";
		charAccessor.init(testString);
		charAccessor.setIndex(5);
		
		assertFalse(charAccessor.isEmptyTillEndOfLine());
		assertEquals(5, charAccessor.getIndex());
	}
	
}
