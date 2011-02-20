package iconcerto.wiki.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: Page
 *
 */
@Entity
@Table(schema="wiki", name="pages")
public class PageEntity {

	private String name;

	@Id
	@Column(name="name", length=512)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
