package iconcerto.wiki.parser;

public interface ElementParsers {

	Elements parse(ParseBundle parseBundle);
	
	Elements getCurrentParent();
	
	void setCurrentParent(Elements parent);
	
}
