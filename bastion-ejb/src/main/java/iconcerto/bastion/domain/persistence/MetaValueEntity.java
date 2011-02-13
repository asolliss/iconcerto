package iconcerto.bastion.domain.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: MetaKeys
 *
 */
@Entity
@Table(schema="bastion", name="meta_keys")
@SequenceGenerator(name="meta_keys_id_gen", sequenceName="bastion.meta_keys_id_seq", allocationSize=4)
public class MetaValueEntity {

	private Integer id;
	private String name;
	private String description;

	@Id
	@Column(name="id")
	@GeneratedValue(generator="meta_keys_id_gen")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="name", length=256)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="description", length=1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
