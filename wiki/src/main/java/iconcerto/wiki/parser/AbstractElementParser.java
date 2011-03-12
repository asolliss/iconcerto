package iconcerto.wiki.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractElementParser implements ElementParsers {

	private Element currentParent;
	private List<ElementParsers> elementParsers = new ArrayList<ElementParsers>();
	private List<ElementParsers> unmodifiableElementParsers =
		Collections.unmodifiableList(elementParsers);
	private Map<Object, Delimiter> delimiters = new LinkedHashMap<Object, Delimiter>();
	private Delimiter defaultDelimiter;
	
	@Override
	public Element getCurrentParent() {
		return currentParent;
	}

	@Override
	public void setCurrentParent(Element parent) {
		this.currentParent = parent;
	}
	
	public void addElementParser(ElementParsers elementParser) {
		elementParsers.add(elementParser);
	}

	public List<ElementParsers> getElementsParsers() {
		return unmodifiableElementParsers;
	}

	@Override
	public Delimiter getDelimiter(Object type) {
		if (Delimiter.DEFAULT.equals(type)) return defaultDelimiter;
		return delimiters.get(type);
	}

	@Override
	public void addDelimiter(Delimiter delimiter) {
		if (Delimiter.DEFAULT.equals(delimiter.getType())) {
			defaultDelimiter = delimiter;
		}
		delimiters.put(delimiter.getType(), delimiter);
	}

	@Override
	public Delimiter getDefaultDelimiter() {
		return defaultDelimiter;
	}

	@Override
	public Collection<Delimiter> getDelimiters() {
		return delimiters.values();
	}
	
}
