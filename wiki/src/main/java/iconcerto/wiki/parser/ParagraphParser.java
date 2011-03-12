package iconcerto.wiki.parser;

public class ParagraphParser extends AbstractElementParser {
	
	public ParagraphParser() {
		addElementParser(new SpanParser());
		addElementParser(new LinkParser());
	}

	@Override
	public Element parse(ParseBundle parseBundle) {
		CharAccessor ca = parseBundle.getCharAccessor();
		
		Paragraph paragraph = new Paragraph();
		paragraph.setParent(getCurrentParent());
		paragraph.setFirstCharIndex(ca.getIndex());
		StringBuilder sb = new StringBuilder();

		int positionOfElement = 0;
		int snippetBeginning = ca.getIndex();
		while (ca.hasNext()) {
			char c = ca.getChar();
			ca.returnChar();
			if (c == '\n') {
				break;
			}			
			
			Element element = null;
			for (ElementParsers elementParser: getElementsParsers()) {
				elementParser.setCurrentParent(paragraph);
				element = elementParser.parse(parseBundle);
				if (element != null) {					
					break;
				}
			}
			
			if (element != null) {
				element.setRelativePosition(positionOfElement);
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
		
		paragraph.setLastCharIndex(ca.getIndex()-1);
		paragraph.setText(sb.toString());
		ca.skipLine();
		
		return paragraph;
	}

}
