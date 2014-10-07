package an.dpr.manteniket.pages;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.components.LinkPanel;
import an.dpr.manteniket.dao.ActivitiesDAO;
import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.template.ManteniketPage;

public class ActivitiesListPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ActivitiesListPage.class);
    @SpringBean
    private ActivitiesDAO dao;

    public ActivitiesListPage() {
	super();
	Link lnkAdd = new Link("lnkAdd"){

	    @Override
	    public void onClick() {
		PageParameters params = new PageParameters();
		params.set(ManteniketContracts.ID, 0);
		setResponsePage(ActivitiesPage.class);
	    }
	    
	};
	lnkAdd.add(new Label("lblAdd", new ResourceModel("btn.add")));
	add(lnkAdd);
	add(new Label("headDate", new ResourceModel("head.date")));
	add(new Label("headKm", new ResourceModel("head.km")));
	add(new Label("headBike", new ResourceModel("head.bike")));
	listado();
    }
    
    private void listado() {
	log.debug("iniciando listado");
	List<Activity> list = dao.findAll();
	ListDataProvider<Activity> data = new ListDataProvider<Activity>(list);
	DataView<Activity> dataView = new DataView<Activity>("rows", data){

	    @Override
	    protected void populateItem(Item<Activity> item) {
		Activity act = item.getModelObject();
		RepeatingView rv = new RepeatingView("dataRow");
		rv.add(new Label(rv.newChildId(), new PropertyModel(item.getModel(), "date")));
		rv.add(new Label(rv.newChildId(), new PropertyModel(item.getModel(), "km")));
		rv.add(new Label(rv.newChildId(), new PropertyModel(item.getModel(), "bike.codBici")));
		PageParameters params = new PageParameters();
		params.set(ManteniketContracts.ID, act.getActivityId());
		rv.add(new LinkPanel(rv.newChildId(), params, ActivitiesPage.class, getString("btn.edit")));
		rv.add(new LinkPanel(rv.newChildId(), params, ActivityDeletePage.class, getString("btn.delete")));
		item.add(rv);
	    }
	    
	}; 
	dataView.setItemsPerPage(10);
	add(dataView);
	add(new BootstrapPagingNavigator("pagingNavigator", dataView));
    }


}
