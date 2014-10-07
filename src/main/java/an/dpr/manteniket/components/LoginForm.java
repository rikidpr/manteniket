package an.dpr.manteniket.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.LoginPage;
import an.dpr.manteniket.MainPage;
import an.dpr.manteniket.pages.ComponentsPage;
import an.dpr.manteniket.security.AppSecurity;

public class LoginForm extends Form {

    private static final Logger log = LoggerFactory.getLogger(LoginForm.class);

    private TextField<String> usernameField;
    private PasswordTextField passwordField;
    private Label loginStatus;

    public LoginForm(String id) {
	super(id);
	initComponents();
    }

    private void initComponents() {
	log.debug("inicio");// aspectos!

	usernameField = new TextField<String>("username", Model.of(""));
	passwordField = new PasswordTextField("password", Model.of(""));
	loginStatus = new Label("loginStatus", Model.of(""));

	add(usernameField);
	add(passwordField);
	add(loginStatus);
    }

    public final void onSubmit() {
	String username = (String) usernameField.getDefaultModelObject();
	String password = (String) passwordField.getDefaultModelObject();
	boolean login = AppSecurity.login(username, AppSecurity.getEncryptPassword(password));
	if (login){
	    setResponsePage(MainPage.class);
//	    loginStatus.setDefaultModelObject("Congratulations!");
	} else {
	    setResponsePage(LoginPage.class);
	    loginStatus.setDefaultModelObject("Wrong username or password!");
	}
    }
}
