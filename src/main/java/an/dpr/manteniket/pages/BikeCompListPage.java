package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;
import static an.dpr.manteniket.bean.ManteniketContracts.BTN_RETURN;
import static an.dpr.manteniket.bean.ManteniketContracts.ID;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
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

import an.dpr.manteniket.components.LinkPanel;
import an.dpr.manteniket.components.ManteniketTable;
import an.dpr.manteniket.dao.ComponentUsesDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

public class BikeCompListPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(BikeCompListPage.class);
    @SpringBean
    private ComponentUsesDAO dao;

    public BikeCompListPage(PageParameters params) {
	super();

	Long id = params.get(ID).toLongObject();
	final PageParameters pp = new PageParameters();
	pp.add("bikeId", id);
	BootstrapForm form = new BootstrapForm("form");
	BootstrapButton btnAdd = new BootstrapButton("btnAdd", BTN_ADD){

	    private static final long serialVersionUID = 1L;
	    
	    @Override
	    public void onSubmit(){
		setResponsePage(ComponentUsePage.class, pp);
	    }
	
	};
	btnAdd.setLabel(new ResourceModel("btn.add"));
	form.add(btnAdd);
	BootstrapButton retBtn = new BootstrapButton("btnReturn", BTN_RETURN){
	    
	    
	    private static final long serialVersionUID = 1L;
	    
	    @Override
	    public void onSubmit(){
		setResponsePage(BicisListPage.class);
	    }
	    
	};
	retBtn.setLabel(new ResourceModel("btn.return"));
	form.add(retBtn);
	add(form);
	listado(id);
    }
    
    private void listado(final Long id) {
	log.debug("iniciando listado");
	Bici bike = new Bici();
	bike.setIdBici(id);
	List<ComponentUse> list = dao.findByBike(bike);
	ListDataProvider<ComponentUse> data = new ListDataProvider<ComponentUse>(list);
	DataView<ComponentUse> dataView = new DataView<ComponentUse>("rows", data){

	    @Override
	    protected void populateItem(Item<ComponentUse> item) {
		ComponentUse use = item.getModelObject();
		RepeatingView rv = new RepeatingView("dataRow");
		rv.add(new Label(rv.newChildId(), new PropertyModel<ComponentUse>(item.getModel(), "bike.codBici")));
		rv.add(new Label(rv.newChildId(), new PropertyModel<ComponentUse>(item.getModel(), "component.name")));
		rv.add(new Label(rv.newChildId(), new PropertyModel<ComponentUse>(item.getModel(), "initFormat")));
		rv.add(new Label(rv.newChildId(), new PropertyModel<ComponentUse>(item.getModel(), "finishFormat")));
		PageParameters params = new PageParameters();
		params.add(ID, use.getId());
		params.add("bikeId", id);
		rv.add(new LinkPanel(rv.newChildId(), params, ComponentUsePage.class, getString("btn.edit"))); 
//		rv.add(new LinkPanel(rv.newChildId(), params, ComponentUseDeletePage.class, getString("btn.delete"))); 
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
