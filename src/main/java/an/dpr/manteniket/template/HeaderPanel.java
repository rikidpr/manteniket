//package an.dpr.manteniket.template;
//
//import org.apache.wicket.markup.html.basic.Label;
//import org.apache.wicket.markup.html.link.Link;
//import org.apache.wicket.markup.html.panel.Panel;
//
//import an.dpr.manteniket.LoginPage;
//import an.dpr.manteniket.security.AppSecurity;
//
//public class HeaderPanel extends Panel {
//
//    public HeaderPanel(String id) {
//	super(id);
//	add(new Label("userName", AppSecurity.getUserName()));
//	add(new Link("logoutLink") {
//
//	    @Override
//	    public void onClick() {
//		AppSecurity.logout();
//		setResponsePage(LoginPage.class);
//	    }
//	});
//    }
//
//}
