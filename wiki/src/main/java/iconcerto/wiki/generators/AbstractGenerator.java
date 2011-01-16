package iconcerto.wiki.generators;

import java.util.Properties;

import iconcerto.wiki.parser.ParserVisitors;

public abstract class AbstractGenerator implements ParserVisitors, Generators {

	private StringBuilder document;
	private Properties parameters;
	
	public AbstractGenerator() {
		document = new StringBuilder();
		parameters = new Properties();
	}
	
	public AbstractGenerator append(char c) {
		document.append(c);
		return this;
	}

	public AbstractGenerator append(String string) {
		document.append(string);
		return this;
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
	
}
