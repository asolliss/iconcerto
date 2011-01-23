package iconcerto.wiki.parser;

/**
 * Header wiki document element
 * @author ipogudin
 *
 */
public class Header extends AbstractElement {
	
	public static enum Type {H1, H2, H3, H4, H5, H6};	
	
	private Type type;
	private String text;

	public Header() {
		super();
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

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
	
	public static Type intToType(Integer i) {
		return Type.valueOf("H"+String.valueOf(i));		
	}	

}
