package iconcerto.wiki.generators;

public interface Generators {
	
	String getDocument();
	
	void clear();
	
	String getParameter(String key, String defaultValue);
	
	void setParameter(String key, String value);
		
}
