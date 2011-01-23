package iconcerto.wiki.parser;

public class Link extends AbstractElement {
	
	public static enum Type {INTERNAL, EXTERNAL};	

	private String url;
	private Type type;
	private String title;
	
	public Link() {
		super();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void accept(ParserVisitor visitor) {
		visitor.visit(this);
	}

}
