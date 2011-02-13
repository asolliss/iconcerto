package iconcerto.wiki.data;

import javax.ejb.Local;

import iconcerto.wiki.domain.Page;

@Local
public interface WikiDataManager {

	Page getPage(String name);
	
	Page save(Page page);
	
}
