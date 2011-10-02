package iconcerto.wiki.engine.parser;

import java.util.ArrayList;
import java.util.List;

public class RootParser extends AbstractElementParser {

	private List<ElementParsers> elementParsers =
		new ArrayList<ElementParsers>();
	private ParseBundle parseBundle;
	
	public RootParser() {
		elementParsers.add(new HeaderParser());
		elementParsers.add(new TableParser());
		elementParsers.add(new ParagraphParser());
	}
	
	@Override
	public Element parse(ParseBundle parseBundle) {
		this.parseBundle = parseBundle;		
		
		while (parseBundle.getCharAccessor().hasNext()) {
			Element element = null;
			boolean completed = false;
			for (ElementParsers elementParser: elementParsers) {
				element = elementParser.parse(parseBundle);
				if (element != null) {
					recursiveVisit(element);
					completed = true;
					break;
				}
			}
			if (!completed) {
				parseBundle.getCharAccessor().getChar();
			}
		}
		return null;
	}
	
	private void recursiveVisit(Element element) {
		parseBundle.visit(element);
		for (Element child: element.getChildren()) {
			recursiveVisit(child);
		}
	}

}
