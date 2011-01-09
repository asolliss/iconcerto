package iconcerto.wiki.parser;

import java.util.ArrayList;
import java.util.List;

public class RootParser extends AbstractElementParsers {

	private List<ElementParsers> elementParsers =
		new ArrayList<ElementParsers>();
	
	public RootParser() {
		elementParsers.add(new HeaderParser());
		elementParsers.add(new LinkParser());
	}
	
	@Override
	public Elements parse(ParseBundle parseBundle) {
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
					parseBundle.visit(element);
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

}
