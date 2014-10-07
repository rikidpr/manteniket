package an.dpr.manteniket.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.components.ConfirmAction;
import an.dpr.manteniket.components.ConfirmPanel;
import an.dpr.manteniket.dao.ComponentesDAO;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.template.ManteniketPage;

public class ComponentsDeletePage extends ManteniketPage{

    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LoggerFactory.getLogger(ComponentsDeletePage.class);
    @SpringBean
    private ComponentesDAO dao;
    
    public ComponentsDeletePage(final PageParameters params){
	super();
	final Long id = params.get(ManteniketContracts.ID).toLong();
	Component comp = dao.findOne(id);
	StringBuilder texto = new StringBuilder();
	texto.append(comp.getName());
	ConfirmAction actions = new ConfirmAction(){

	    @Override
	    public void accept() {
		delete(id);
		setResponsePage(BicisListPage.class);
	    }

	    @Override
	    public void cancel() {
		setResponsePage(BicisListPage.class);
	    }};
	ConfirmPanel cp = new ConfirmPanel("confirmPanel", texto.toString(), actions);
	add(cp);
    }
    
    private void delete(Long id) {
	try{
	    dao.delete(id);
	    log.debug("component "+id+" deleted");
	} catch(Exception e){
	    log.error("petada borrando", e);
	}
    }
    
}
