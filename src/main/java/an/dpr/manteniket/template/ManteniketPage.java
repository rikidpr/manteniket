package an.dpr.manteniket.template;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import an.dpr.manteniket.WicketApplication;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.dao.IUserDAO;
import an.dpr.manteniket.domain.User;
import an.dpr.manteniket.security.AppSecurity;

public class ManteniketPage extends BootstrapBasePage {
    // public static final String CONTENT_ID = "contentComponent";

    private static final long serialVersionUID = 1L;
    @SpringBean
    private IUserDAO userDao; 
    

    public ManteniketPage() {
	this(null);
    }

    public ManteniketPage(final PageParameters params) {
	super(params);
    }
    
    public <T> Object getBean(String id, Class<T> clase) {
	Object ret = ((WicketApplication) getApplication()).context().getBean(id);
	return clase.cast(ret);
    }
    
    public User getUser(){
	User user = (User)getSession().getAttribute(ManteniketContracts.LOGGED_USER);
	if (user == null){
	    user = userDao.getUser(AppSecurity.getUserName());
	    getSession().setAttribute(ManteniketContracts.LOGGED_USER, user);
	}
	return user;
    }

}
