package an.dpr.manteniket;

import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import an.dpr.manteniket.security.AppSecurity;

public class LogoutPage extends Page{
    
    private static final long serialVersionUID = 1L;

    public LogoutPage(){
	this(null);
    }

    public LogoutPage(PageParameters params){
	AppSecurity.logout();
	setResponsePage(LoginPage.class);
    }

}
