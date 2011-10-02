package iconcerto.wiki.engine.parser;

public class Span extends AbstractElement {

	public static enum Type {
		ITALIC, 
		BOLD, 
		MONOSPACE,
		UNDERLINE, 
		SUPERSCRIPT, 
		SUBSCRIPT, 
		SMALLER, 
		LARGER, 
		STROKE
		};
	
	private String text;
	private Type type;
	
	public Span() {
		super();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public void accept(ParserVisitor visitor) {
		visitor.visit(this);
	}

}
