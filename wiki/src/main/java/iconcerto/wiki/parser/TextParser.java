package iconcerto.wiki.parser;

public class TextParser extends AbstractElementParsers {
	
	public TextParser() {
		addElementParser(new LinkParser());
	}

	@Override
	public Elements parse(ParseBundle parseBundle) {
		CharAccessor ca = parseBundle.getCharAccessor();
		
		Texts text = new Texts();
		text.setParent(getCurrentParent());
		text.setFirstCharIndex(ca.getIndex());
		StringBuilder sb = new StringBuilder();

		int positionOfElement = 0;
		int snippetBeginning = ca.getIndex();
		while (ca.hasNext()) {
			char c = ca.getChar();
			ca.returnChar();
			if (c == '\n') {
				break;
			}			
			
			Elements element = null;
			for (ElementParsers elementParser: getElementsParsers()) {
				element = elementParser.parse(parseBundle);
				if (element != null) {					
					break;
				}
			}
			
			if (element != null) {
				//add an element-child
				text.addChild(element);
				text.put(element, positionOfElement);
				//add a previous text snippet 
				sb.append(ca.getRange(snippetBeginning, element.getFirstCharIndex()));
				snippetBeginning = ca.getIndex();
			}
			else {
				ca.getChar();
				positionOfElement++;
			}
		}
		//add a last text snippet
		sb.append(ca.getRange(snippetBeginning, ca.getIndex()));
		
		text.setLastCharIndex(ca.getIndex()-1);
		text.setText(sb.toString());
		
		return text;
	}

}
