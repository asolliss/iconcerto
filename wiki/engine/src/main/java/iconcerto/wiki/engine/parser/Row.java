package iconcerto.wiki.engine.parser;

/**
 * Row element
 * Contains cells as children
 * @author Ivan Pogudin <i.a.pogudin@gmail.com>
 * @see Table
 * @see Cell
 */
public class Row extends AbstractElement {

	@Override
	public void accept(ParserVisitor visitor) {
		visitor.visit(this);
	}

}
