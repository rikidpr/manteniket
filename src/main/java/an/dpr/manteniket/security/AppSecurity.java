package an.dpr.manteniket.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.LoginPage;


public class AppSecurity {

    private static final Logger log = LoggerFactory.getLogger(AppSecurity.class);
    private static PasswordService passService;
    
    static{
	passService = getPasswordService();
    }
    
    public static boolean isLogin(){
//	Subject user = SecurityUtils.getSubject();
//	if (user.isAuthenticated()){
//	    return true;
//	} else {
//	    return false;
//	}
	return true;
    }

    public static boolean login(String user, String pass) {
	boolean logeado = false;
//
//	AuthenticationToken token = new UsernamePasswordToken(user, pass);
//	Subject subj = SecurityUtils.getSubject();
//	try {
//	    subj.login(token);
	    logeado = true;
//	} catch (AuthenticationException e) {
//	    log.error(user + "/" + pass + " no autenticado", e);
//	}

	return logeado;
    }

    public static final void logout() {
//	Subject currUser = SecurityUtils.getSubject();
//	currUser.logout();
    }

    public static final String getUserName() {
	String ret = null;
//	Subject user = SecurityUtils.getSubject();
//	ret = (String) user.getPrincipal();
	return ret;
    }

    public static final boolean isPermited(Permiso permiso) {
	boolean permitido = false;
//	if (permiso != null) {
//	    Subject user = SecurityUtils.getSubject();
//	    permitido = user.isPermitted(permiso.getPermiso());
//	    log.debug(permiso.getPermiso() + "? " + permitido);
//	}
	permitido = true;//TODO NOOOOL
	return permitido;
    }
    
    public static final String getEncryptPassword(String pass){
	return passService.encryptPassword(pass);
    }
    
    private static final PasswordService getPasswordService(){
	if (passService==null){
	    passService = new ManteniketPasswordService();
	}
	return passService;
    }

    
}
