package iconcerto.wiki.parser;

import java.util.Arrays;

public class HeaderParser extends AbstractElementParser {

	@Override
	public Element parse(ParseBundle parseBundle) {
		Header header = null;
		CharAccessor ca = parseBundle.getCharAccessor();
		int caIndex = ca.getIndex();
		try {
			if (!ca.isFirstCharOfLine()) return header;
			char c = ca.getChar();
			if (c != '=') {
				ca.returnChar();
				return header;
			}
			int index = ca.getIndex()-1;
			int typeNumber = ca.skipPreviousCharRepetition() + 1;
			header = new Header();
			header.setType(Header.intToType(typeNumber));
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
			ca.skipLine();			
		}
		catch (CharAccessorIndexOutOfBoundsException e) {
			ca.setIndex(caIndex);
		}

		return header;
	}

}
