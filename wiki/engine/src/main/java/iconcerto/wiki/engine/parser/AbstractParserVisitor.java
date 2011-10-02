package iconcerto.wiki.engine.parser;

/**
 * Abstract implementation of ParseVisitor to decouple direct dependence between an Interface and an Implementation.
 * You must extend this class instead of ParseVisitor implementation. 
 * That will decouple your class from an changed interface.
 * @author Ivan Pogudin <i.a.pogudin@gmail.com>
 *
 */
public abstract class AbstractParserVisitor implements ParserVisitor {

	@Override
	public void visit(Header header) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Link link) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Paragraph paragraph) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Span span) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Table table) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Row row) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Cell cell) {
		// TODO Auto-generated method stub

	}

	@Override
	public abstract boolean isOnlyRoot();

}
