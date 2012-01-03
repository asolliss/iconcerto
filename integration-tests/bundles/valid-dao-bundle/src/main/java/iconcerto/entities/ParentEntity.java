package iconcerto.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="parent_entities", schema="public")
public class ParentEntity {

	@Id
	private Integer id;
	@Column
	private String name;
	@OneToMany(mappedBy="parentEntity", orphanRemoval=true, cascade=CascadeType.ALL)
	private List<ChildEntity> children;

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

	public List<ChildEntity> getChildren() {
		return children;
	}

	public void setChildren(List<ChildEntity> children) {
		this.children = children;
	}
	
}
