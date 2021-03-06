package an.dpr.manteniket.pages;

import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert.Type;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.components.ConfirmAction;
import an.dpr.manteniket.components.ConfirmPanel;
import an.dpr.manteniket.dao.IBikesDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.exception.ManteniketException;
import an.dpr.manteniket.template.ManteniketPage;

public class BicisDeletePage extends ManteniketPage{

    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LoggerFactory.getLogger(BicisDeletePage.class);
    @SpringBean
    private IBikesDAO dao;
    //componentes
    
    public BicisDeletePage(final PageParameters params){
	super();
	final Long id = params.get(ManteniketContracts.ID).toLong();
	Bici bike = dao.findByIdBici(id);
	StringBuilder texto = new StringBuilder();
	texto.append(bike.getCodBici());
	ConfirmAction actions = new ConfirmAction(){

	    @Override
	    public void accept() {
		PageParameters params = deleteBike(id);
		setResponsePage(BicisListPage.class, params);
	    }

	    @Override
	    public void cancel() {
		setResponsePage(BicisListPage.class);
	    }};
	ConfirmPanel cp = new ConfirmPanel("confirmPanel", texto.toString(), actions);
	add(cp);
    }
    
    private PageParameters deleteBike(Long bikeId) {
	PageParameters params;
	try{
	    dao.delete(bikeId);
	    params = null;
	    log.debug("bike "+bikeId+" deleted");
	} catch(ManteniketException e){
	    params = new PageParameters();
	    if (ManteniketException.CANT_DELETE_DEPENDENCIES == e.getCode()){
		params.add(ManteniketContracts.NOTIFICATION, "delete.bike.dependencies");
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
