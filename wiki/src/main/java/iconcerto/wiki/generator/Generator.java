package iconcerto.wiki.generator;

import iconcerto.wiki.parser.AbstractParserVisitor;

public abstract class Generator extends AbstractParserVisitor {
	
	public abstract String getDocument();
	
	public abstract void clear();
	
	public abstract String getParameter(String key, String defaultValue);
	
	public abstract void setParameter(String key, String value);
		
}
