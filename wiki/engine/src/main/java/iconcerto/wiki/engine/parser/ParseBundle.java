package iconcerto.wiki.engine.parser;

public interface ParseBundle {

	CharAccessor getCharAccessor();
	
	void visit(Element element);
	
}
