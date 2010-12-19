package iconcerto.bastion.entities;

import java.io.Serializable;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Users
 *
 */
@Entity
@Table(schema="bastion", name="users")
@SequenceGenerator(name="users_id_gen", sequenceName="bastion.users_id_seq", allocationSize=4)
public class UserEntities implements Serializable {

	private static final long serialVersionUID = 8771325894640387481L;
	
	private Integer id;
	private String username;
	private String password;
	private Boolean blocked;

	public UserEntities() {
		super();
	}  
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="users_id_gen")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}   
	
	@Column(name="username", length=255)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}   
	
	@Column(name="password", length=32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}   
	
	@Column(name="blocked")
	public Boolean isBlocked() {
		return this.blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}
   
}