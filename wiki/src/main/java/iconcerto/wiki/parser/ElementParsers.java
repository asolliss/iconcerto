package iconcerto.wiki.parser;

public interface ElementParsers {

	Element parse(ParseBundle parseBundle);
	
	Element getCurrentParent();
	
	void setCurrentParent(Element parent);
	
}
