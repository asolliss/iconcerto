package iconcerto.bastion.domain;

import java.io.Serializable;

import iconcerto.bastion.entities.UserEntities;

public class Users implements Serializable {

	private static final long serialVersionUID = 7908864736364501709L;
	
	private Integer id;
	private String username;
	
	public Users() {
		
	}
	
	public Users(UserEntities userEntity) {
		this.id = userEntity.getId();
		this.username = userEntity.getUsername();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
