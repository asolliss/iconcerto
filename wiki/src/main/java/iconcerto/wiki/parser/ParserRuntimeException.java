package iconcerto.wiki.parser;

public class ParserRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -2671036939235225317L;

	public ParserRuntimeException() {
		super();
	}

	public ParserRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParserRuntimeException(String message) {
		super(message);
	}

	public ParserRuntimeException(Throwable cause) {
		super(cause);
	}

}
