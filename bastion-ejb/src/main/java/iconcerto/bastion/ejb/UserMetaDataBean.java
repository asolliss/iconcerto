package iconcerto.bastion.ejb;

import iconcerto.bastion.domain.User;
import iconcerto.bastion.domain.persistence.UserEntity;
import iconcerto.bastion.ejb.UserMetaDataRemote;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserMetaDataBean implements UserMetaDataRemote {

	@PersistenceContext(unitName="bastion")
	private EntityManager em;
	
	@Override
	public User getUser(String username) {
		User user = null;
		try {			
			if (username != null) {
				UserEntity userEntities = (UserEntity) em.createQuery("select u from UserEntities u where u.username = :username")
											.setParameter("username", username).getSingleResult();
				user = new User(userEntities);
			}
			else {
				user = new User();
			}
    	}
		catch (Throwable e) {
			e.printStackTrace(System.err);
		}
		return user;
	}
	
}
