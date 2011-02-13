package iconcerto.wiki.data;

import javax.persistence.EntityManager;

import iconcerto.wiki.domain.Page;

public interface WikiDAO {
	
	boolean isEntityManagerNeed();
	
	void setEntityManager(EntityManager em);

	Page getPage(String name) throws DAORuntimeException;
	
	Page save(Page page) throws DAORuntimeException;
	
}
