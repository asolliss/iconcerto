package iconcerto.wiki.ejb;
import iconcerto.wiki.domain.Page;

import javax.ejb.Remote;

@Remote
public interface WikiBeanRemote {

	/**
	 * Get a page by name.
	 * @param name
	 * @return
	 */
	Page getPage(String name);
	
	/**
	 * Save a new revision or create a page.
	 * @param page
	 * @return
	 */
	Page save(Page page);
	
	/**
	 * Convert wiki code to xhtml.
	 * @param page
	 * @return
	 */
	Page convert(Page page);
	
}
