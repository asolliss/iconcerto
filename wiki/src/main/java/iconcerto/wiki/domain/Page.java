package iconcerto.wiki.domain;

import java.io.Serializable;
import java.util.Date;

import iconcerto.wiki.domain.persistence.PageEntity;
import iconcerto.wiki.domain.persistence.RevisionEntity;

public class Page implements Serializable {

	private static final long serialVersionUID = -9055888697243829641L;
	
	private String name;
	private Date timestamp;
	private String revision;
	private String code;
	private String XHTML;
	
	private boolean changed;
	
	public Page() {
		code = "";
		XHTML = "";
	}
	
	public Page(PageEntity pageEntity, RevisionEntity revisionEntity) {
		name = pageEntity.getName();
		timestamp = revisionEntity.getTimestamp();
		revision = revisionEntity.getRevision();
		code = revisionEntity.getCode();
		XHTML = revisionEntity.getXHTML();
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getRevision() {
		return revision;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		change(this.code, code);
		this.code = code;
	}

	public String getXHTML() {
		return XHTML;
	}

	public void setXHTML(String XHTML) {
		change(this.XHTML, XHTML);
		this.XHTML = XHTML;
	}

	public boolean isChanged() {
		return changed;
	}
	
	public boolean isNew() {
		return timestamp == null;
	}
	
	private void change(String oldValue, String newValue) {
		if ((oldValue != null && !oldValue.equals(newValue))) {
			changed = true;
		}		
	}
	
}
