package iconcerto.wiki.generator;

import iconcerto.wiki.parser.ParserVisitor;

public abstract class Generator implements ParserVisitor {
	
	public abstract String getDocument();
	
	public abstract void clear();
	
	public abstract String getParameter(String key, String defaultValue);
	
	public abstract void setParameter(String key, String value);
		
}
