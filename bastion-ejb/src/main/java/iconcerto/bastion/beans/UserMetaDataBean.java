package iconcerto.bastion.beans;

import iconcerto.bastion.beans.UserMetaDataRemote;
import iconcerto.bastion.domain.Users;
import iconcerto.bastion.entities.UserEntities;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserMetaDataBean implements UserMetaDataRemote {

	@PersistenceContext(unitName="bastion")
	private EntityManager em;
	
	@Override
	public Users getUser(String username) {
		Users user = null;
		try {			
			if (username != null) {
				UserEntities userEntities = (UserEntities) em.createQuery("select u from UserEntities u where u.username = :username")
											.setParameter("username", username).getSingleResult();
				user = new Users(userEntities);
			}
			else {
				user = new Users();
			}
    	}
		catch (Throwable e) {
			e.printStackTrace(System.err);
		}
		return user;
	}
	
}
