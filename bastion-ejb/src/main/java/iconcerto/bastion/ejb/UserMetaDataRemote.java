package iconcerto.bastion.ejb;
import iconcerto.bastion.domain.User;

import javax.ejb.Remote;

@Remote
public interface UserMetaDataRemote {
	
	User getUser(String username);
	
}
