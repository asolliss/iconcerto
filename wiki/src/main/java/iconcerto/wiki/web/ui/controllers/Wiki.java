package iconcerto.wiki.web.ui.controllers;

import iconcerto.wiki.domain.Page;
import iconcerto.wiki.services.facades.WikiService;

import static iconcerto.wiki.web.ui.controllers.Wiki.States.*;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="wiki")
@SessionScoped
public class Wiki {

	protected static enum States{BROWSING, EDITING};
	
	private States state = BROWSING;
	private Page page;
	
	@EJB
	private WikiService wikiService;
	
	public String getName() {
		loadPage(null);
		return page !=null?page.getName():null;
	}
	
	public void setName(String name) {		
		loadPage(name);
	}
	
	public String getHTML() {
		return page.getXHTML();
	}	
	
	public String getCode() {
		return page.getCode();
	}
	
	public void setCode(String code) {
		page.setCode(code);
	}
	
	public void browse() {
		page = wikiService.convert(page);
		state = BROWSING;
	}
	
	public void edit() {
		state = EDITING;
	}
	
	public boolean isBrowsing() {
		return BROWSING.equals(state);
	}
	
	public boolean isEditing() {
		return EDITING.equals(state);
	}
	
	public boolean isSaved() {
		return !page.isChanged() && !page.isNew();
	}
	
	public void save() {
		page = wikiService.save(page);
	}
	
	public void rollBack() {
		String name = page.getName();
		page = null;
		loadPage(name);
	}

	private void loadPage(String name) {
		if (page == null || (name != null && !page.getName().equals(name))) {
			if (name == null) name = "Index";
			page = wikiService.getPage(name);
		}
	}
	
}
