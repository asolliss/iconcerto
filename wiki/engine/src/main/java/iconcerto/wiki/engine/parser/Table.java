package iconcerto.wiki.engine.parser;

/**
 * Table element
 * Contains rows elements as children 
 * @author Ivan Pogudin <i.a.pogudin@gmail.com>
 * @see Row
 */
public class Table extends AbstractElement {

	@Override
	public void accept(ParserVisitor visitor) {
		visitor.visit(this);
	}

}
