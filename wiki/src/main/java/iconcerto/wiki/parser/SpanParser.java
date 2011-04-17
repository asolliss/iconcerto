package iconcerto.wiki.parser;

import iconcerto.wiki.parser.Span.Type;
import iconcerto.wiki.parser.exceptions.ParsingIndexOutOfBoundsException;

public class SpanParser extends AbstractElementParser {

	public SpanParser() {		
		addDelimiter(new Delimiter(Type.ITALIC, "''".toCharArray(), "''".toCharArray()));
		addDelimiter(new Delimiter(Type.BOLD, "\"\"".toCharArray(), "\"\"".toCharArray()));
		addDelimiter(new Delimiter(Type.MONOSPACE, "`".toCharArray(), "`".toCharArray()));
		addDelimiter(new Delimiter(Type.UNDERLINE, "__".toCharArray(), "__".toCharArray()));
		addDelimiter(new Delimiter(Type.SUPERSCRIPT, "^".toCharArray(), "^".toCharArray()));
		addDelimiter(new Delimiter(Type.SUBSCRIPT, ",,".toCharArray(), ",,".toCharArray()));
		addDelimiter(new Delimiter(Type.SMALLER, "~-".toCharArray(), "-~".toCharArray()));
		addDelimiter(new Delimiter(Type.LARGER, "~+".toCharArray(), "+~".toCharArray()));
		addDelimiter(new Delimiter(Type.STROKE, "--(".toCharArray(), ")--".toCharArray()));
	}
	
	@Override
	public Element parse(ParseBundle parseBundle) {
		return parseSpan(getCurrentParent(), parseBundle);
	}
	
	private Span parseSpan(Element parent, ParseBundle parseBundle) {
		Span span = null;
		CharAccessor ca = parseBundle.getCharAccessor();
		int caIndex = ca.getIndex();
		try {
			Delimiter currentDelimiter = null;
			for (Delimiter delimiter: getDelimiters()) {
				if (ca.match(delimiter.getLeftDelimiter())) {
					currentDelimiter = delimiter;
					break;
				}
			}
			if (currentDelimiter != null) {
				span = new Span();
				int firstCharIndex = ca.getIndex() - currentDelimiter.getLeftDelimiter().length;
				ca.pushStopSequence(currentDelimiter.getRightDelimiter());
				StringBuilder text = new StringBuilder();
				
				int positionOfElement = 0;
				int snippetBeginning = ca.getIndex();
				Span child = null;
				while (ca.hasNext()) {					
					child = parseSpan(span, parseBundle);
					
					if (child != null) {
						child.setRelativePosition(positionOfElement);
						//add a previous text snippet 
						text.append(ca.getRange(snippetBeginning, child.getFirstCharIndex()));
						snippetBeginning = ca.getIndex();
					}
					else {
						ca.getChar();
						positionOfElement++;
					}
				}
				ca.popStopSequence();
				text.append(ca.getRange(snippetBeginning, ca.getIndex()));
				ca.setIndex(ca.getIndex()+currentDelimiter.getRightDelimiter().length);
				int lastCharIndex = ca.getIndex() - 1;
				
				span.setFirstCharIndex(firstCharIndex);
				span.setLastCharIndex(lastCharIndex);
				span.setParent(parent);
				span.setType((Type) currentDelimiter.getType());
				span.setText(text.toString());
			}
		}
		catch (ParsingIndexOutOfBoundsException e) {
			ca.setIndex(caIndex);
		}
		
		return span;
	}

}
