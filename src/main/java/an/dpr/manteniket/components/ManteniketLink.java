package an.dpr.manteniket.components;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

/**
 * Links de manteniket para tablas
 * @author ricardo.saez-martinez@hp.com
 *
 * @param <T> pagina destino
 */
public class ManteniketLink<T>  extends Panel {
    
    private BootstrapBookmarkablePageLink<T> link; 
    private static final String LINK_ID = "manteniketLink";

    private static final long serialVersionUID = -1104797132284998200L;

   /* public <P extends ManteniketPage> ManteniketLink(String componentId, Class<P> pageClass, PageParameters parameters,
	    Type type) {
	super(componentId);
	link = new BootstrapBookmarkablePageLink<T>(componentId, pageClass, parameters, type);
    }
    */
    
    public  <P extends ManteniketPage> ManteniketLink(String componentId, final Class<P> pageClass, final IModel<String> label) {
        this(componentId, pageClass, new PageParameters(), label);
    }
    
    public  <P extends ManteniketPage> ManteniketLink(String componentId, final Class<P> pageClass, final PageParameters parameters, final IModel<String> label) {
	super(componentId);
	link = new BootstrapBookmarkablePageLink<T>(LINK_ID, pageClass, parameters, Buttons.Type.Menu);
        link.setLabel(label);
	add(link);
    }

    public void setIconType(IconType iconType) {
	link.setIconType(iconType);
    }
    
    

}
