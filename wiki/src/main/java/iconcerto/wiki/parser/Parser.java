package iconcerto.wiki.parser;

import java.util.ArrayList;
import java.util.List;

public class Parser implements ParseBundle {

	private List<ParserVisitors> visitors = new ArrayList<ParserVisitors>();
	private CharAccessor charAccessor;
	private RootParser rootParser = new RootParser();
		
	public Parser() {

	}

	public void addVisitor(ParserVisitors visitor) {
		visitors.add(visitor);
	}
	
	public void removeVisitor(ParserVisitors visitor) {
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

	public void visit(Elements element) {
		for (ParserVisitors visitor: visitors) {
			element.accept(visitor);
		}
	}
	
}
