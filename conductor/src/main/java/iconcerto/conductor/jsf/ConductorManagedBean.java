package iconcerto.conductor.jsf;

import iconcerto.bastion.domain.User;
import iconcerto.bastion.ejb.UserMetaDataRemote;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
public class ConductorManagedBean {
	
	@EJB
	private UserMetaDataRemote userMetaData;
	
	private User user;
	
	public String getUsername() {
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		String username = null;
		if (
				user != null && 
				!user.getUsername().equals(
						String.valueOf(httpServletRequest.getUserPrincipal())
						)
				) {
			user = null;
		}
		
		if (user == null) {			
			if (httpServletRequest.getUserPrincipal() != null) {
				user = userMetaData.getUser(String.valueOf(httpServletRequest.getUserPrincipal()));
			}			
		}
		if (user != null) {
			username = user.getUsername();
		}
		else {
			username = "none";
		}
		
		return username;
	}
	
}
