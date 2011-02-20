package iconcerto.wiki.service.data;

import javax.ejb.Local;

import iconcerto.wiki.domain.Page;

@Local
public interface WikiDataService {

	Page getPage(String name);
	
	Page save(Page page);
	
}
