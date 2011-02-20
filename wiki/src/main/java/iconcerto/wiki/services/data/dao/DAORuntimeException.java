package iconcerto.wiki.services.data.dao;

public class DAORuntimeException extends RuntimeException {

	private static final long serialVersionUID = -5495282216359204232L;
	
	public DAORuntimeException(Throwable cause) {
		super(cause);
		setStackTrace(cause.getStackTrace());
	}

}
