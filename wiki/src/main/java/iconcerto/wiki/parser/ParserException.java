package iconcerto.wiki.parser;

public class ParserException extends Exception {

	private static final long serialVersionUID = 2356114783460840900L;

	public ParserException() {
		super();
	}

	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParserException(String message) {
		super(message);
	}

	public ParserException(Throwable cause) {
		super(cause);
	}

}
