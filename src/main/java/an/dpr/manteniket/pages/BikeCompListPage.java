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
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.components.LinkPanel;
import an.dpr.manteniket.dao.ComponentUsesDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.template.ManteniketPage;

public class BikeCompListPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(BikeCompListPage.class);
    @SpringBean
    private ComponentUsesDAO dao;

    public BikeCompListPage(PageParameters params) {
	super();

	Long id = params.get(ManteniketContracts.ID).toLongObject();
	final PageParameters pp = new PageParameters();
	pp.add("bikeId", id);
	Link addLnk = new Link("addLnk"){

	    @Override
	    public void onClick() {
		setResponsePage(ComponentUsePage.class, pp);
	    }
	    
	};
	add(addLnk);
	Link retBtn = new Link("retLnk"){
	    
	    @Override
	    public void onClick() {
		setResponsePage(BicisListPage.class);
	    }
	    
	};
	add(retBtn);
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
		rv.add(new Label(rv.newChildId(), new PropertyModel(item.getModel(), "bike.codBici")));
		rv.add(new Label(rv.newChildId(), new PropertyModel(item.getModel(), "component.name")));
		rv.add(new Label(rv.newChildId(), new PropertyModel(item.getModel(), "init")));
		rv.add(new Label(rv.newChildId(), new PropertyModel(item.getModel(), "finish")));
		PageParameters params = new PageParameters();
		params.add(ManteniketContracts.ID, use.getId());
		params.add("bikeId", id);
		rv.add(new LinkPanel(rv.newChildId(), params, ComponentUsePage.class, getString("btn.edit"))); 
//		rv.add(new LinkPanel(rv.newChildId(), params, ComponentUseDeletePage.class, getString("btn.delete"))); 
		item.add(rv);
	    }
	    
	}; 
	dataView.setItemsPerPage(3);
	add(dataView);
	add(new BootstrapPagingNavigator("pagingNavigator", dataView));
    }


}
