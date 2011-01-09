package iconcerto.wiki.parser;

public interface ParserVisitors {
	
	void visit(Headers header);
	
	void visit(Links link);
	
	void visit(Texts text);

	void visit(Root root);
}
