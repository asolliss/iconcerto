package iconcerto.wiki.domain.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: Page
 *
 */
@Entity
@Table(schema="wiki", name="revisions")
public class RevisionEntity {

	private String revision;
	private PageEntity page;
	private Date timestamp;	
	private String code;
	private String XHTML;

	@Id
	@Column(name="revision")
	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="page")
	public PageEntity getPage() {
		return page;
	}

	public void setPage(PageEntity page) {
		this.page = page;
	}

	@Column(name="timestamp")
	@Temporal(TemporalType.TIMESTAMP)	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Column(name="code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name="xhtml")
	public String getXHTML() {
		return XHTML;
	}

	public void setXHTML(String XHTML) {
		this.XHTML = XHTML;
	}
	
}
