package iconcerto.wiki.parser;

public class Paragraph extends AbstractElement {

	private String text;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}	

	@Override
	public void accept(ParserVisitor visitor) {
		visitor.visit(this);
	}

}
