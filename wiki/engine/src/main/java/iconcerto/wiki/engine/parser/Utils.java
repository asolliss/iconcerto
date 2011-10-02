package iconcerto.wiki.engine.parser;

/**
 * Parser's utils
 * @author ipogudin
 *
 */
public abstract class Utils {

	/**
	 * Match a sequence in the current index of a charAccessor.
	 * If match is true then the current index is set at a position after a sequence
	 * else a position of the current index is not changed.
	 * @param sequence
	 * @param charAccessor
	 * @return
	 */
	public static boolean match(char[] sequence, CharAccessor charAccessor) {
		int index = charAccessor.getIndex();		
		
		int i = 0;
		int length = sequence.length;
		while (charAccessor.hasNext() && length > i) {
			char c = charAccessor.getChar();
			if (sequence[i] != c) {
				break;
			}
			i++;
		}
		
		if (length == i) return true;
		
		charAccessor.setIndex(index);
		
		return false;
	}
	
}
