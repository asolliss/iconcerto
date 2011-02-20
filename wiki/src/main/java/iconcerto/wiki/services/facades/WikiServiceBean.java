package iconcerto.wiki.services.facades;

import iconcerto.wiki.domain.Page;
import iconcerto.wiki.generator.Generator;
import iconcerto.wiki.generator.XHTML;
import iconcerto.wiki.parser.Parser;
import iconcerto.wiki.service.data.WikiDataService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Session Bean implementation class WikiBean
 */
@Named
@Stateless
public class WikiServiceBean implements WikiService {

	@Inject
	private Parser parser;
	@Inject @XHTML
	private Generator generator;
	@EJB
	private WikiDataService dataService;
	
    @PostConstruct
    public void init() {
    	parser.addVisitor(generator);    	
    }

	@Override
	public Page getPage(String name) {
		return dataService.getPage(name);
	}

	@Override
	public Page save(Page page) {
		return dataService.save(page);
	}

	@Override
	public Page convert(Page page) {
		try {
			parser.parse(page.getCode());
			page.setXHTML(generator.getDocument());
		}
		finally {
			generator.clear();
		}
		return page;
	}
    
}
