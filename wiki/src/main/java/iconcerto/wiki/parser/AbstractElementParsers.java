package iconcerto.wiki.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractElementParsers implements ElementParsers {

	private Elements currentParent;
	private List<ElementParsers> elementParsers = new ArrayList<ElementParsers>();
	private List<ElementParsers> unmodifiableElementParsers =
		Collections.unmodifiableList(elementParsers);
	
	@Override
	public Elements getCurrentParent() {
		return currentParent;
	}

	@Override
	public void setCurrentParent(Elements parent) {
		this.currentParent = parent;
	}

	public void addElementParser(ElementParsers elementParser) {
		elementParsers.add(elementParser);
	}
	
	public List<ElementParsers> getElementsParsers() {
		return unmodifiableElementParsers;
	}
}
