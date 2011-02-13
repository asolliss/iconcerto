package iconcerto.bastion.domain.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: MetaValues
 *
 */
@Entity
@Table(schema="bastion", name="meta_values")
@SequenceGenerator(name="meta_values_id_gen", sequenceName="bastion.meta_values_id_seq", allocationSize=4)
public class MetaKeyEntity {

	private Integer id;
	private MetaKeyEntity key;
	private UserEntity user;
	private String value;

	@Id
	@Column(name="id")
	@GeneratedValue(generator="meta_values_id_gen")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="meta_key")
	public MetaKeyEntity getKey() {
		return key;
	}

	public void setKey(MetaKeyEntity key) {
		this.key = key;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="\"user\"")
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@Column(name="value", length=4096)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
