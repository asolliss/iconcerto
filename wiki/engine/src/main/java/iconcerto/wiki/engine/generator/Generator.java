package iconcerto.wiki.engine.generator;

import iconcerto.wiki.engine.parser.AbstractParserVisitor;

public abstract class Generator extends AbstractParserVisitor {
	
	public abstract String getDocument();
	
	public abstract void clear();
	
	public abstract String getParameter(String key, String defaultValue);
	
	public abstract void setParameter(String key, String value);
		
}
