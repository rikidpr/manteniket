package an.dpr.manteniket.security;

import org.springframework.beans.factory.annotation.Autowired;

import an.dpr.manteniket.dao.IUserDAO;
import an.dpr.manteniket.domain.User;

/**
 * Informacion con scope=session del usuario logueado
 * @author rsaez
 *
 */
public class UserInfo {
    
    @Autowired private IUserDAO dao;
    private User user;
    
    public User getUser(){
	if (user == null){
	    user = dao.getUser();
	}
	return user;
    }

}
