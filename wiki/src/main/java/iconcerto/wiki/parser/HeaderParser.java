package iconcerto.wiki.parser;

import java.util.Arrays;

public class HeaderParser extends AbstractElementParsers {

	@Override
	public Elements parse(ParseBundle parseBundle) {
		Headers header = null;
		CharAccessor ca = parseBundle.getCharAccessor();
		int caIndex = ca.getIndex();
		try {
			if (!ca.isFirstCharOfLine()) return header;
			char c = ca.getChar();
			if (c == '=') {
				int index = ca.getIndex()-1;
				int typeNumber = ca.missPreviousCharRepetition() + 1;
				header = new Headers();
				header.setParent(getCurrentParent());
				header.setType(Headers.intToType(typeNumber));
				header.setFirstCharIndex(index);
				
				char[] sequence = new char[typeNumber];
				Arrays.fill(sequence, '=');
				ca.lookForAtSingleLine(sequence);			
							
				header.setText(
						new String(ca.getRange(
								header.getFirstCharIndex()+typeNumber, 
								ca.getIndex()-typeNumber
								)
							)
						);
				header.setLastCharIndex(ca.getIndex()-1);
			}
			else {
				ca.returnChar();
			}
		}
		catch (CharAccessorIndexOutOfBoundsException e) {
			ca.setIndex(caIndex);
		}

		return header;
	}

}
