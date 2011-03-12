package iconcerto.wiki.parser;

public interface ParserVisitor {

	void visit(Header header);
	
	void visit(Link link);
	
	void visit(Paragraph paragraph);
	
	void visit(Span span);
	
	boolean isOnlyRoot();
}
