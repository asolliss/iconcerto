package iconcerto.wiki.parser;

import java.util.Collection;

public interface ElementParsers {

	Element parse(ParseBundle parseBundle);
	
	Element getCurrentParent();
	
	void setCurrentParent(Element parent);
	
	Delimiter getDelimiter(Object type);
	
	void addDelimiter(Delimiter delimiter);
	
	Delimiter getDefaultDelimiter();
	
	Collection<Delimiter> getDelimiters();
}
