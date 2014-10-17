package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
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

import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.components.LinkPanel;
import an.dpr.manteniket.components.ManteniketTable;
import an.dpr.manteniket.dao.BicisDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

public class BicisListPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory
	    .getLogger(BicisListPage.class);
    @SpringBean
    private BicisDAO dao;

    public BicisListPage() {
	super();

	BootstrapForm form = new BootstrapForm("form");
	BootstrapButton btnAdd = new BootstrapButton("btnAdd", BTN_ADD){

	    private static final long serialVersionUID = 1L;
	    
	    @Override
	    public void onSubmit(){
		setResponsePage(BicisPage.class);
	    }
	
	};
	btnAdd.setLabel(new ResourceModel("btn.add"));
	form.add(btnAdd);
	add(form);
	listado();
    }

    private void listado() {
	log.debug("iniciando listado");
	List<Bici> bicis = dao.findAll();
	ListDataProvider<Bici> data = new ListDataProvider<Bici>(bicis);
	DataView<Bici> dataView = new DataView<Bici>("rows", data) {

	    @Override
	    protected void populateItem(Item<Bici> item) {
		Bici bici = item.getModelObject();
		RepeatingView rv = new RepeatingView("dataRow");
		rv.add(new Label(rv.newChildId(), new PropertyModel(item
			.getModel(), "codBici")));
		rv.add(new Label(rv.newChildId(), new PropertyModel(item
			.getModel(), "descripcion")));
		rv.add(new Label(rv.newChildId(), new PropertyModel(item
			.getModel(), "tipo")));
		PageParameters params = new PageParameters();
		params.add(ManteniketContracts.ID, bici.getIdBici());
		params.add(ManteniketContracts.ENTITY, ManteniketContracts.Entity.BIKE);
		params.add(ManteniketContracts.RETURN_PAGE, this.getClass().getName());
		rv.add(new LinkPanel(rv.newChildId(), params,
			BikeCompListPage.class, getString("btn.components")));
		rv.add(new LinkPanel(rv.newChildId(), params, BicisPage.class,
			getString("btn.edit")));
		rv.add(new LinkPanel(rv.newChildId(), params,
			BicisDeletePage.class, getString("btn.delete")));

		item.add(rv);
	    }

	};
	dataView.setItemsPerPage(3);
	ManteniketTable table = new ManteniketTable("table");
	table.add(dataView);
	add(table);
	add(new BootstrapPagingNavigator("pagingNavigator", dataView));
    }

}
