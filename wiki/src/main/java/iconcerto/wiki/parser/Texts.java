package iconcerto.wiki.parser;

import java.util.HashMap;
import java.util.Map;

public class Texts extends AbstractElements {

	private String text;
	private Map<Elements, Integer> positionToElement =
		new HashMap<Elements, Integer>();
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public int getPosition(Elements element) {
		return positionToElement.get(element);
	}
	
	public void put(Elements element, Integer position) {
		positionToElement.put(element, position);
	}

	@Override
	public void accept(ParserVisitors visitor) {
		visitor.visit(this);
	}

}
