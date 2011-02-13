package iconcerto.wiki.ejb;

import iconcerto.wiki.data.WikiDataManager;
import iconcerto.wiki.domain.Page;
import iconcerto.wiki.generator.Generator;
import iconcerto.wiki.parser.Parser;

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
public class WikiBean implements WikiBeanRemote {

	@Inject
	private Parser parser;
	@Inject @XHTML
	private Generator generator;
	@EJB
	private WikiDataManager dataManager;
	
    @PostConstruct
    public void init() {
    	parser.addVisitor(generator);    	
    }

	@Override
	public Page getPage(String name) {
		return dataManager.getPage(name);
	}

	@Override
	public Page save(Page page) {
		return dataManager.save(page);
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
