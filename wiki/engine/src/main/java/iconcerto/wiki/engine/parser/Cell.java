package iconcerto.wiki.engine.parser;

/**
 * Cell element
 * Contains paragraphs as children
 * @author Ivan Pogudin <i.a.pogudin@gmail.com>
 *
 */
public class Cell extends AbstractElement {

	@Override
	public void accept(ParserVisitor visitor) {
		visitor.visit(this);
	}

}
