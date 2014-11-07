package an.dpr.manteniket.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.components.ConfirmAction;
import an.dpr.manteniket.components.ConfirmPanel;
import an.dpr.manteniket.dao.IComponentsDAO;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.exception.ManteniketException;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert.Type;

public class ComponentsDeletePage extends ManteniketPage{

    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LoggerFactory.getLogger(ComponentsDeletePage.class);
    @SpringBean
    private IComponentsDAO dao;
    
    public ComponentsDeletePage(final PageParameters params){
	super();
	final Long id = params.get(ManteniketContracts.ID).toLong();
	Component comp = dao.findOne(id);
	StringBuilder texto = new StringBuilder();
	texto.append(comp.getName());
	ConfirmAction actions = new ConfirmAction(){

	    @Override
	    public void accept() {
		PageParameters params= delete(id);
		setResponsePage(ComponentsListPage.class, params);
	    }

	    @Override
	    public void cancel() {
		setResponsePage(ComponentsListPage.class);
	    }};
	ConfirmPanel cp = new ConfirmPanel("confirmPanel", texto.toString(), actions);
	add(cp);
    }
    
    
    private PageParameters delete(Long id) {
	PageParameters params;
	try{
	    dao.delete(id);
	    params = null;
	    log.debug("component "+id+" deleted");
	} catch(ManteniketException e){
	    params = new PageParameters();
	    if (ManteniketException.CANT_DELETE_DEPENDENCIES == e.getCode()){
		params.add(ManteniketContracts.NOTIFICATION, "delete.component.dependencies");
	    } else {
		params.add(ManteniketContracts.NOTIFICATION, "delete.error");
	    }
	    params.add(ManteniketContracts.NOTIFICATION_TYPE, Type.Danger);
	} catch(Exception e){
	    params = new PageParameters();
	    params.add(ManteniketContracts.NOTIFICATION, "delete.error");
	    params.add(ManteniketContracts.NOTIFICATION_TYPE, Type.Danger);
	}
	return params;
    }
    
}
