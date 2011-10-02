package iconcerto.wiki.engine.generator;

import java.util.Properties;

public abstract class AbstractGenerator extends Generator {

	private StringBuilder document;
	private Properties parameters; 
	
	public AbstractGenerator() {
		document = new StringBuilder();
		parameters = new Properties();
	}	

	@Override
	public String getDocument() {
		return document.toString();
	}

	@Override
	public void clear() {
		document = new StringBuilder();
	}

	@Override
	public String getParameter(String key, String defaultValue) {
		return parameters.getProperty(key, defaultValue);		
	}

	@Override
	public void setParameter(String key, String value) {
		parameters.setProperty(key, value);
	}
	
	protected AbstractGenerator append(char c) {
		document.append(c);
		return this;
	}

	protected AbstractGenerator append(String string) {
		document.append(string);
		return this;
	}
	
}
