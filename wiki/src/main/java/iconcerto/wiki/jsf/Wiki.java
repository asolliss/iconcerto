package iconcerto.wiki.jsf;

import iconcerto.wiki.domain.Page;
import iconcerto.wiki.ejb.WikiBeanRemote;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="wiki")
@SessionScoped
public class Wiki {
	
	public static final int BROWSING = 1;
	public static final int EDITING = 2;
	
	private int state = BROWSING;
	private Page page;
	
	@EJB
	private WikiBeanRemote wikiBean;
	
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
		page = wikiBean.convert(page);
		state = BROWSING;
	}
	
	public void edit() {
		state = EDITING;
	}
	
	public boolean isBrowsing() {
		return BROWSING == state;
	}
	
	public boolean isEditing() {
		return EDITING == state;
	}
	
	public boolean isSaved() {
		return !page.isChanged() || !page.isNew();
	}
	
	public void save() {
		page = wikiBean.save(page);
	}

	private void loadPage(String name) {
		if (page == null || (name != null && !page.getName().equals(name))) {
			if (name == null) name = "Index";
			page = wikiBean.getPage(name);
		}
	}
	
}
