package iconcerto.wiki.parser;

import iconcerto.wiki.parser.exceptions.ConfigurationException;
import iconcerto.wiki.parser.exceptions.ParsingException;
import iconcerto.wiki.parser.exceptions.ParsingIndexOutOfBoundsException;

import java.util.Arrays;

/**
 * access to symbols  
 * @author ipogudin
 *
 */
public class CharAccessor {
	
	public final static char NULL_CHAR = '\0';
	private final static int STOP_SEQUENCE_STACK_SIZE = 32;
	
	private int index;
	private char[] characters;
	private char[][] stopSequenceStack;
	private int stopSequenceIndex;
	
	public CharAccessor(String characters) {
		init(characters);
	}
	
	public void init(String characters) {
		clear();
		this.characters = characters.toCharArray();
		
		stopSequenceStack = new char[STOP_SEQUENCE_STACK_SIZE][];
		stopSequenceIndex = -1;
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
		return characters.length > index && !matchStopSequence();
	}
	
	/**
	 * Get the current char and increase the index
	 * @return
	 */
	public char getChar() throws ParsingIndexOutOfBoundsException {
		if (!hasNext()) throw new ParsingIndexOutOfBoundsException();
		return characters[index++];
	}
	
	public boolean isFirstCharOfLine() {
		return index == 0 || characters[index-1] == '\n';
	}
	
	/**
	 * Get the current char
	 * @return
	 */
	public char getCharWithoutIncrement() throws ParsingIndexOutOfBoundsException {
		if (!hasNext()) throw new ParsingIndexOutOfBoundsException();
		return characters[index];
	}
	
	/**
	 * Decrease the index
	 */
	public void returnChar() {
		index--;
	}

	/**
	 * Skip a repetition of previous char
	 * @return factor of repetition
	 */
	public int skipPreviousCharRepetition() {
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
	 * Set the index on the first char of the next line.
	 * If this line is last then set the index on the end of the code 
	 */
	public void skipLine() {
		while (hasNext() && (getChar() != '\n'));
	}
	
	/**
	 * Look for a char sequence from a current position to the end of a document.
	 * @param sequence
	 * @throws ParsingException
	 */
	public void lookFor(char[] sequence) throws ParsingException {
		lookFor(sequence, false);
	}
	
	/**
	 * Look for a char sequence from a current position to the end of a line.
	 * @param sequence
	 * @throws ParsingException
	 */
	public void lookForAtSingleLine(char[] sequence) throws ParsingException {
		lookFor(sequence, true);
	}
	
	/**
	 * Match a sequence in the current index of a charAccessor.
	 * If match is true then the current index is set at a position after a sequence
	 * else a position of the current index is not changed.
	 * @param sequence
	 * @return
	 * @throws ParsingException
	 */
	public boolean match(char[] sequence) throws ParsingException {
		return Utils.match(sequence, this);
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
	
	/**
	 * Push a stop sequence into the stop sequence stack
	 * @param stopSequence
	 * @throws ConfigurationException
	 */
	public void pushStopSequence(char[] stopSequence) throws ConfigurationException {
		if (stopSequenceIndex + 1 >= STOP_SEQUENCE_STACK_SIZE) {
			throw new ConfigurationException("CharAccessors's stop sequence stack is full");
		}
		stopSequenceStack[++stopSequenceIndex] = stopSequence;
	}
	
	/**
	 * Pop a stop sequence from the stop sequence stack
	 * @return
	 * @throws ConfigurationException
	 */
	public char[] popStopSequence() throws ConfigurationException {
		if (stopSequenceIndex < 0) {
			throw new ConfigurationException("CharAccessors's stop sequence stack already is empty");
		}
		
		char[] stopSequence = stopSequenceStack[stopSequenceIndex];
		stopSequenceStack[stopSequenceIndex] = null;
		stopSequenceIndex--;
		return stopSequence;
	}
	
	/**
	 * Check empty space from the current index to end of line.
	 * If true then the current index is set at first char of new line.
	 * @return true if empty
	 * @throws ParsingException
	 */
	public boolean isEmptyTillEndOfLine() throws ParsingException {
		boolean result = false;
		
		int index = getIndex();
		
		try {
			while (hasNext()) {
				char c = getChar();			
				if (c == '\n') {
					result = true;
					break;
				}
				if (c != ' ') {
					result = false;
					break;
				}
			}
		}
		finally {
			if (!result) {
				setIndex(index);
			}
		}
		
		return result;
	}
	
	/**
	 * Match a current stop sequence in a current position
	 * @return
	 */
	private boolean matchStopSequence() {
		
		if (stopSequenceIndex < 0) return false;
		
		int localIndex = index;
		char[] stopSequence = stopSequenceStack[stopSequenceIndex];
		
		if (stopSequence[0] != characters[localIndex]) return false;
				
		int length = characters.length;
		int localStopSequenceIndex = 1;
		int stopSequenceLength = stopSequence.length;
		
		localIndex++;
		for (
				; 
				localIndex < length && localStopSequenceIndex < stopSequenceLength
				; 
				localIndex++,
				localStopSequenceIndex++
				) {			
			if (stopSequence[localStopSequenceIndex] !=
						characters[localIndex]) break;
		}
		
		return localStopSequenceIndex == stopSequenceLength;
	}
	
	private void lookFor(char[] sequence, boolean single) throws ParsingException {
		if (sequence == null || sequence.length < 1) {
			throw new ParsingException("The char sequence is empty or null.");
		} 
		
		boolean found = false;
		while (hasNext()) {
			char c = getChar();
			if (single && c == '\n') break;
			if (Utils.match(sequence, this)) {
				found = true;
				break;
			}
		}
		
		if (!found) {
			throw new ParsingException("The char sequence is not found.");
		}
	}
	
}
