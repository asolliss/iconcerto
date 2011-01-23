package iconcerto.wiki.parser;

public interface ParseBundle {

	CharAccessor getCharAccessor();
	
	void visit(Element element);
	
}
