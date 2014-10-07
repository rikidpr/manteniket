package an.dpr.manteniket.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.components.ConfirmAction;
import an.dpr.manteniket.components.ConfirmPanel;
import an.dpr.manteniket.dao.BicisDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.template.ManteniketPage;

public class BicisDeletePage extends ManteniketPage{

    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LoggerFactory.getLogger(BicisDeletePage.class);
    @SpringBean
    private BicisDAO dao;
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
		deleteBike(id);
		setResponsePage(BicisListPage.class);
	    }

	    @Override
	    public void cancel() {
		setResponsePage(BicisListPage.class);
	    }};
	ConfirmPanel cp = new ConfirmPanel("confirmPanel", texto.toString(), actions);
	add(cp);
    }
    
    private void deleteBike(Long bikeId) {
	try{
	    dao.delete(bikeId);
	    log.debug("bike "+bikeId+" deleted");
	} catch(Exception e){
	    log.error("petada borrando", e);
	}
    }
    
}
