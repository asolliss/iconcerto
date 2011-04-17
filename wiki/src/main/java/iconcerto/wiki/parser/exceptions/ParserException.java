package iconcerto.wiki.parser.exceptions;

public class ParserException extends RuntimeException {

	private static final long serialVersionUID = -2671036939235225317L;

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
