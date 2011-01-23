package iconcerto.wiki.parser;

import java.util.List;

public interface Element {

	void accept(ParserVisitor visitor);
	
	Element getParent();
	
	void setParent(Element parent);
	
	List<Element> getChildren();
	
	void addChild(Element child);
	
	void removeChild(Element child);
	
	int getFirstCharIndex();
	
	void setFirstCharIndex(int index);
	
	int getLastCharIndex();
	
	void setLastCharIndex(int index);
	
	int getLevel();
	
	int getRelativePosition();
	
	void setRelativePosition(int relativePosition);
	
}
