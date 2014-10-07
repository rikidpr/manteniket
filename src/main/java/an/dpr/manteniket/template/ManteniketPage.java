package an.dpr.manteniket.template;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import an.dpr.manteniket.WicketApplication;
import an.dpr.manteniket.dao.ComponentesDAO;

public class ManteniketPage extends BootstrapBasePage {
    // public static final String CONTENT_ID = "contentComponent";

    private Component headerPanel;
    private Component menuPanel;
    private Component footerPanel;

    public ManteniketPage() {
	this(null);
    }

    public ManteniketPage(final PageParameters params) {
	super(params);
    }
    
    public <T> Object getBean(String id, Class<T> clase) {
	Object ret = ((WicketApplication) getApplication()).context().getBean(
		id);
	return clase.cast(ret);
    }

    public Component getHeaderPanel() {
	return headerPanel;
    }

    public void setHeaderPanel(Component headerPanel) {
	this.headerPanel = headerPanel;
    }

    public Component getMenuPanel() {
	return menuPanel;
    }

    public void setMenuPanel(Component menuPanel) {
	this.menuPanel = menuPanel;
    }

    public Component getFooterPanel() {
	return footerPanel;
    }

    public void setFooterPanel(Component footerPanel) {
	this.footerPanel = footerPanel;
    }
}
