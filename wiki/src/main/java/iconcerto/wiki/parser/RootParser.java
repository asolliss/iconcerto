package iconcerto.wiki.parser;

import java.util.ArrayList;
import java.util.List;

public class RootParser extends AbstractElementParsers {

	private List<ElementParsers> elementParsers =
		new ArrayList<ElementParsers>();
	private ParseBundle parseBundle;
	
	public RootParser() {
		elementParsers.add(new HeaderParser());
		elementParsers.add(new TextParser());
	}
	
	@Override
	public Elements parse(ParseBundle parseBundle) {
		this.parseBundle = parseBundle;
		Root root = new Root();
		for (ElementParsers elementParser: elementParsers) {
			elementParser.setCurrentParent(root);
		}
		
		while (parseBundle.getCharAccessor().hasNext()) {
			Elements element = null;
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
		return root;
	}
	
	private void recursiveVisit(Elements element) {
		parseBundle.visit(element);
		for (Elements child: element.getChildren()) {
			recursiveVisit(child);
		}
	}

}
