package iconcerto.wiki.parser;

import java.util.Arrays;

/**
 * access to symbols  
 * @author ipogudin
 *
 */
public class CharAccessor {
	
	public final static char NULL_CHAR = '\0';
	
	private int index;
	private char[] characters;
	
	public CharAccessor(String characters) {
		init(characters);
	}
	
	public void init(String characters) {
		clear();
		this.characters = characters.toCharArray();
	}
	
	public void clear() {
		index = 0;
		characters = null;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getPreviousIndex() {
		return index - 1;
	}	
	
	public boolean hasNext() {
		return characters.length > index;
	}
	
	/**
	 * Get the current char and increase the index
	 * @return
	 */
	public char getChar() throws CharAccessorException {
		if (!hasNext()) throw new CharAccessorIndexOutOfBoundsException();
		return characters[index++];
	}
	
	public boolean isFirstCharOfLine() {
		return index == 0 || characters[index-1] == '\n';
	}
	
	/**
	 * Get the current char
	 * @return
	 */
	public char getCharWithoutIncrement() throws CharAccessorException {
		if (!hasNext()) throw new CharAccessorIndexOutOfBoundsException();
		return characters[index];
	}
	
	/**
	 * Decrease the index
	 */
	public void returnChar() {
		index--;
	}

	/**
	 * Miss a repetition of previous char
	 * @return factor of repetition
	 */
	public int missPreviousCharRepetition() {
		char c = characters[index-1];
		int i = 0;
		while (hasNext() && (getChar() == c)) {			
			i++;
		}
		if (hasNext()) {
			returnChar();
		}
		return i;
	}
	
	/**
	 * Look for a char sequence from a current position to the end of a document.
	 * @param sequence
	 * @throws ParserRuntimeException
	 */
	public void lookFor(char[] sequence) throws ParserRuntimeException {
		lookFor(sequence, false);
	}
	
	/**
	 * Look for a char sequence from a current position to the end of a line.
	 * @param sequence
	 * @throws ParserRuntimeException
	 */
	public void lookForAtSingleLine(char[] sequence) throws ParserRuntimeException {
		lookFor(sequence, true);
	}
	
	/**
	 * Get a certain range from characters 
	 * @param beginning
	 * @param end
	 * @return
	 */
	public char[] getRange(int beginning, int end) {		
		return Arrays.copyOfRange(characters, beginning, end);
	}
	
	private void lookFor(char[] sequence, boolean single) throws ParserRuntimeException {
		if (sequence == null || sequence.length < 1) {
			throw new ParserRuntimeException("The char sequence is empty or null.");
		} 
		
		int targetIndex = sequence.length;
		int i = 0;
		while (hasNext()) {
			char c = getChar();
			if (single && c == '\n') break;
			if (sequence[i] == c) {
				i++;
				if (i == targetIndex) break;				
			}
			else if (i > 0) {
				i = 0;
			}
		}
		
		if (i != targetIndex) {
			throw new ParserRuntimeException("The char sequence is not found.");
		}
	}
	
}
