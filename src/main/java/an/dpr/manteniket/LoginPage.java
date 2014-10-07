package an.dpr.manteniket;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.components.LoginForm;
import an.dpr.manteniket.template.ManteniketPage;

public class LoginPage extends ManteniketPage {

    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);

    public LoginPage() {
	super();
	log.debug("login!");
//	getMenuPanel().setVisible(false); TODO delete me. ya no es necesario con bootstrap version
	add(new LoginForm("loginForm"));
    }

}
