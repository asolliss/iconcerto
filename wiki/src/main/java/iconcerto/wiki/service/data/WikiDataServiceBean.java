package iconcerto.wiki.service.data;

import iconcerto.wiki.domain.Page;
import iconcerto.wiki.services.data.dao.PostgreSQL;
import iconcerto.wiki.services.data.dao.WikiDAO;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
@Stateless
public class WikiDataServiceBean implements WikiDataService {

	@PersistenceContext(unitName="wiki")
	private EntityManager em;
	
	@Inject @PostgreSQL
	private WikiDAO wikiDAO;
	
	@PostConstruct
	public void init() {
		if (wikiDAO.isEntityManagerNeed()) {
			wikiDAO.setEntityManager(em);
		}
	}
	
	@Override
	public Page getPage(String name) {
		return wikiDAO.getPage(name);
	}

	@Override
	public Page save(Page page) {
		return wikiDAO.save(page);
	}

}
