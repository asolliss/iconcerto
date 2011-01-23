package iconcerto.wiki.parser;

public interface ParserVisitor {

	void visit(Header header);
	
	void visit(Link link);
	
	void visit(Paragraph paragraph);
	
	boolean isOnlyRoot();
}
