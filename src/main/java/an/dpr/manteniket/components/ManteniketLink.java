package an.dpr.manteniket.components;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import an.dpr.manteniket.bean.ManteniketBean;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.bean.ManteniketContracts.Entity;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.pages.BicisPage;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;

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
    
    public static <P> ManteniketLink<P> createLink(String componentId, IModel<? extends ManteniketBean> rowModel, Entity entity, 
	     Class<? extends ManteniketPage> destination, IModel<String> modelLink, IconType iconType){
	PageParameters params = new PageParameters();
	ManteniketBean bean = rowModel.getObject();
	params.add(ManteniketContracts.ID, bean.getId());
	if(entity != null){
	    params.add(ManteniketContracts.ENTITY, entity);
	}
	ManteniketLink<P> ml = new ManteniketLink<P>(componentId, destination, params, modelLink);
	if (iconType != null){
	    ml.setIconType(iconType);
	}
	return ml;
    }

}
