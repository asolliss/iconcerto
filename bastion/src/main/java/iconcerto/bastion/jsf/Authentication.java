package iconcerto.bastion.jsf;

import java.io.IOException;
import java.net.URLEncoder;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The development realization of authentication. Don't use for production! 
 * @author ipogudin
 *
 */
@ManagedBean
@SessionScoped
public class Authentication {
	
	private String username;
	private String password;
	
	public Authentication() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getLoggedIn() {
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		return username != null && httpServletRequest.getUserPrincipal() != null;
	}

	public Boolean getIncorrectUsernameAndPassword() {
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		return username != null && httpServletRequest.getUserPrincipal() == null;
	}

	/**
	 * It's a temporary method realization. Authentication only for development.
	 * This crutch is need for Glassfish Single Sign On working.
	 */
	public void logIn() {
		HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		try {
			httpServletResponse.sendRedirect(
					"/bastion/j_security_check?j_username=" +
					URLEncoder.encode(getUsername(), "UTF-8") +
					"&j_password=" +
					URLEncoder.encode(getPassword(), "UTF-8")
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void logOut() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
	}
	
}
