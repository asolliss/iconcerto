package iconcerto.wiki.parser;

import java.util.ArrayList;
import java.util.List;

public class Parser implements ParseBundle {

	private List<ParserVisitor> visitors = new ArrayList<ParserVisitor>();
	private CharAccessor charAccessor;
	private RootParser rootParser = new RootParser();
		
	public Parser() {

	}

	public void addVisitor(ParserVisitor visitor) {
		visitors.add(visitor);
	}
	
	public void removeVisitor(ParserVisitor visitor) {
		visitors.remove(visitor);
	}
	
	public void parse(String code) {
		charAccessor = new CharAccessor(code);
		parse();
	}
	
	@Override
	public CharAccessor getCharAccessor() {
		return charAccessor;
	}

	private void parse() {
		rootParser.parse(this);
	}

	@Override
	public void visit(Element element) {
		for (ParserVisitor visitor: visitors) {
			if (visitor.isOnlyRoot() && element.getParent() != null) continue;
			element.accept(visitor);
		}
	}
	
}
