package iconcerto.wiki.parser;

public class Delimiter {
	
	public final static String DEFAULT = "default";
	
	private Object type;
	private char[] leftDelimiter;
	private char[] rightDelimiter;
	
	public Delimiter(Object type, char[] leftDelimiter, char[] rightDelimiter) {
		super();
		this.type = type;
		this.leftDelimiter = leftDelimiter;
		this.rightDelimiter = rightDelimiter;
	}

	public Object getType() {
		return type;
	}

	public void setType(Object type) {
		this.type = type;
	}

	public char[] getLeftDelimiter() {
		return leftDelimiter;
	}

	public void setLeftDelimiter(char[] leftDelimiter) {
		this.leftDelimiter = leftDelimiter;
	}

	public char[] getRightDelimiter() {
		return rightDelimiter;
	}

	public void setRightDelimiter(char[] rightDelimiter) {
		this.rightDelimiter = rightDelimiter;
	}
	
}
