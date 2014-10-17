package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;
import static an.dpr.manteniket.bean.ManteniketContracts.BTN_RETURN;
import static an.dpr.manteniket.bean.ManteniketContracts.ENTITY;
import static an.dpr.manteniket.bean.ManteniketContracts.ID;

import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.StringValueConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.bean.ManteniketContracts.Entity;
import an.dpr.manteniket.components.LinkPanel;
import an.dpr.manteniket.components.ManteniketTable;
import an.dpr.manteniket.dao.ComponentUsesDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

public class BikeCompListPage extends ManteniketPage {
    
    public static void main(String[] args){
	String s="[Page class = an.dpr.manteniket.pages.ComponentsListPage, id = 3";
	String[] aux = s.substring(14).split(",");
	System.out.println(aux[0]);
    }

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(BikeCompListPage.class);
    @SpringBean
    private ComponentUsesDAO dao;

    public BikeCompListPage(PageParameters params) throws StringValueConversionException, InstantiationException, IllegalAccessException, ClassNotFoundException {
	super();

	Long id = params.get(ID).toLongObject();
	Entity entity = params.get(ENTITY).toEnum(Entity.class);
	final String retPageClass= params.get(ManteniketContracts.RETURN_PAGE).toString();
	final Class retPage = Class.forName(retPageClass);
	final PageParameters pp = new PageParameters();
	pp.add(ManteniketContracts.SOURCE_ID, id);
	pp.add(ManteniketContracts.ENTITY, entity);
	pp.add(ManteniketContracts.RETURN_PAGE, this.getPageReference());
	
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
		setResponsePage(retPage);
	    }
	    
	};
	retBtn.setLabel(new ResourceModel("btn.return"));
	form.add(retBtn);
	add(form);
	listado(id, entity);
    }
    
    private void listado(final Long id, final Entity entity) {
	log.debug("iniciando listado");
	List<ComponentUse> list = getList(id, entity);
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
		params.add(ManteniketContracts.SOURCE_ID, id);
		params.add(ManteniketContracts.ENTITY, entity);
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

    private List<ComponentUse> getList(Long id, Entity model) {
	List<ComponentUse> list = null;
	switch(model){
	case BIKE:
	    list = getListByBike(id);
	    break;
	case COMPONENT:
	    list = getListByComponent(id);
	    break;
	}
	return list;
    }
    
    private List<ComponentUse> getListByBike(Long id) {
	Bici bike = new Bici();
	bike.setIdBici(id);
	List<ComponentUse> list = dao.findByBike(bike);
	return list;
    }

    private List<ComponentUse> getListByComponent(Long id) {
	Component c = new Component();
	c.setId(id);
	List<ComponentUse> list = dao.findByComponent(c);
	return list;
    }


}
