package an.dpr.manteniket.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.components.ConfirmAction;
import an.dpr.manteniket.components.ConfirmPanel;
import an.dpr.manteniket.dao.IActivityDao;
import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.template.ManteniketPage;

public class ActivityDeletePage extends ManteniketPage{

    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LoggerFactory.getLogger(ActivityDeletePage.class);
    @SpringBean
    private IActivityDao dao;
    //componentes
    
    public ActivityDeletePage(final PageParameters params){
	super();
	final Long id = params.get(ManteniketContracts.ID).toLong();
	Activity act = dao.findById(id);
	StringBuilder texto = new StringBuilder();
	texto.append(act.getDate());
	ConfirmAction actions = new ConfirmAction(){

	    @Override
	    public void accept() {
		deleteActivity(id);
		setResponsePage(ActivitiesListPage.class);
	    }

	    @Override
	    public void cancel() {
		setResponsePage(ActivitiesListPage.class);
	    }};
	ConfirmPanel cp = new ConfirmPanel("confirmPanel", texto.toString(), actions);
	add(cp);
    }
    
    private void deleteActivity(Long activityId) {
	dao.delete(activityId);
	log.debug("activity "+activityId+" deleted");
    }
    
}
