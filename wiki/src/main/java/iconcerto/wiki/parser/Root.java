package iconcerto.wiki.parser;

public class Root extends AbstractElements {

	@Override
	public void accept(ParserVisitors visitor) {
		visitor.visit(this);
	}

}
