package iconcerto.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="child_entities", schema="public")
public class ChildEntity {

	@Id
	private Integer id;
	@Column
	private String name;
	@ManyToOne(cascade=CascadeType.ALL)
	private ParentEntity parentEntity;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ParentEntity getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(ParentEntity parentEntity) {
		this.parentEntity = parentEntity;
	}
	
}
