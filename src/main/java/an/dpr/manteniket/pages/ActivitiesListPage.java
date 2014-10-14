package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;

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

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.components.LinkPanel;
import an.dpr.manteniket.components.ManteniketTable;
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
	BootstrapForm form = new BootstrapForm("form");
	BootstrapButton btnAdd = new BootstrapButton("btnAdd", BTN_ADD){

	    private static final long serialVersionUID = 1L;
	    
	    @Override
	    public void onSubmit(){
		PageParameters params = new PageParameters();
		params.set(ManteniketContracts.ID, 0);
		setResponsePage(ActivitiesPage.class);
	    }
	
	};
	btnAdd.setLabel(new ResourceModel("btn.add"));
	form.add(btnAdd);
	add(form);
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
	ManteniketTable table = new ManteniketTable("table");
	table.add(dataView);
	table.add(new Label("headDate", new ResourceModel("head.date")));
	table.add(new Label("headKm", new ResourceModel("head.km")));
	table.add(new Label("headBike", new ResourceModel("head.bike")));
	add(table);
	add(new BootstrapPagingNavigator("pagingNavigator", dataView));
    }


}
