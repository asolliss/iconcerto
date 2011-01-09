package iconcerto.wiki.parser;

/**
 * Header wiki document element
 * @author ipogudin
 *
 */
public class Headers extends AbstractElements {
	
	public static enum Types {H1, H2, H3, H4, H5, H6};	
	
	private Types type;
	private String text;

	public Headers() {
		super();
	}

	public Types getType() {
		return type;
	}

	public void setType(Types type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void accept(ParserVisitors visitor) {
		visitor.visit(this);
	}
	
	public static Types intToType(Integer i) {
		return Types.valueOf("H"+String.valueOf(i));		
	}	

}
