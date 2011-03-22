package iconcerto.wiki.parser;

public interface ParserVisitor {

	void visit(Header header);
	
	void visit(Link link);
	
	void visit(Paragraph paragraph);
	
	void visit(Span span);
	
	void visit(Table table);
	
	void visit(Row row);
	
	void visit(Cell cell);
	
	boolean isOnlyRoot();
}
