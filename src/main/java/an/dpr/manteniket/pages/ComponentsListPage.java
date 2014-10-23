package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import an.dpr.manteniket.WicketApplication;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.components.LinkPanel;
import an.dpr.manteniket.components.ManteniketTable;
import an.dpr.manteniket.dao.ActivitiesDAO;
import an.dpr.manteniket.dao.ComponentesDAO;
import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.template.ManteniketPage;

public class ComponentsListPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ComponentsListPage.class);
    @SpringBean
    private ComponentesDAO dao;
    @SpringBean
    private ActivitiesDAO actDao;

    public ComponentsListPage() {
	super();
	BootstrapForm form = new BootstrapForm("form");
	BootstrapButton btnAdd = new BootstrapButton("btnAdd", BTN_ADD){

	    private static final long serialVersionUID = 1L;
	    
	    @Override
	    public void onSubmit(){
		setResponsePage(ComponentsPage.class);
	    }
	
	};
	btnAdd.setLabel(new ResourceModel("btn.add"));
	form.add(btnAdd);
	add(form);
	listado();
    }
    
    private void listado() {
	log.debug("iniciando listado");
	listadoOld();
    }
    
    @Deprecated
    private void listadoOld(){
	log.debug("iniciando listado");
	List<Component> componentes = dao.findAll();
	ListDataProvider<Component> data = new ListDataProvider<Component>(componentes);
	DataView<Component> dataView = new DataView<Component>("rows", data){

	    @Override
	    protected void populateItem(Item<Component> item) {
		Component component = item.getModelObject();
		Double km = getKmComponent(component);
		RepeatingView rv = new RepeatingView("dataRow");
		rv.add(new Label(rv.newChildId(), new PropertyModel(item.getModel(), "name")));
		rv.add(new Label(rv.newChildId(), new PropertyModel(item.getModel(), "type")));
		rv.add(new Label(rv.newChildId(), Model.of(km)));
		PageParameters params = new PageParameters();
		params.add(ManteniketContracts.ID, component.getId());
		params.add(ManteniketContracts.ENTITY, ManteniketContracts.Entity.COMPONENT);
		params.add(ManteniketContracts.RETURN_PAGE, this.getClass().getName());
		rv.add(new LinkPanel(rv.newChildId(), params, BikeCompListPage.class, getString("btn.uses")));
		rv.add(new LinkPanel(rv.newChildId(), params, ComponentsPage.class, getString("btn.edit"))); 
		rv.add(new LinkPanel(rv.newChildId(), params, ComponentsDeletePage.class, getString("btn.delete"))); 
		item.add(rv);
	    }

	    
	}; 
	dataView.setItemsPerPage(3);
	ManteniketTable table = new ManteniketTable("table");
	table.add(dataView);
	add(table);
	add(new BootstrapPagingNavigator("pagingNavigator", dataView));
    }

    private Double getKmComponent(Component pComp) {
	Double km = 0.0;
	Component component = dao.findOne(pComp.getId());
	Iterator<ComponentUse> it = component.getComponentUses().iterator();
	while(it.hasNext()){
	    ComponentUse use = it.next();
	    Date fin = use.getFinish() != null ? use.getFinish() : new Date();
	    List<Activity> list = actDao.findByBikeAndDates(use.getBike(), use.getInit(), fin);
	    for(Activity act : list){
		km += act.getKm();
	    }
	}
	return km;
    }

}
