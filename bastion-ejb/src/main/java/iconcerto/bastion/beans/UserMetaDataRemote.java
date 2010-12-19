package iconcerto.bastion.beans;
import iconcerto.bastion.domain.Users;

import javax.ejb.Remote;

@Remote
public interface UserMetaDataRemote {
	
	Users getUser(String username);
	
}
