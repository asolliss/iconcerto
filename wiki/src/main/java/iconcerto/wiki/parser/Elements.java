package iconcerto.wiki.parser;

import java.util.List;

public interface Elements {

	void accept(ParserVisitors visitor);
	
	Elements getParent();
	
	void setParent(Elements parent);
	
	List<Elements> getChildren();
	
	void addChild(Elements child);
	
	void removeChild(Elements child);
	
	int getFirstCharIndex();
	
	void setFirstCharIndex(int index);
	
	int getLastCharIndex();
	
	void setLastCharIndex(int index);
	
	int getLevel();
	
}
