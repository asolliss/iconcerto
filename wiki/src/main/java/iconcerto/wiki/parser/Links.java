package iconcerto.wiki.parser;

public class Links extends AbstractElements {
	
	public static enum Types {INTERNAL, EXTERNAL};	

	private String url;
	private Types type;
	private String title;
	
	public Links() {
		super();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Types getType() {
		return type;
	}

	public void setType(Types type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void accept(ParserVisitors visitor) {
		visitor.visit(this);
	}

}
