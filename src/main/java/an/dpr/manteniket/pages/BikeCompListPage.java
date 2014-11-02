package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;
import static an.dpr.manteniket.bean.ManteniketContracts.BTN_RETURN;
import static an.dpr.manteniket.bean.ManteniketContracts.ENTITY;
import static an.dpr.manteniket.bean.ManteniketContracts.ID;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
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
import org.apache.wicket.util.string.StringValueConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import an.dpr.manteniket.MainPage;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.bean.ManteniketContracts.Entity;
import an.dpr.manteniket.components.LinkPanel;
import an.dpr.manteniket.components.ManteniketTable;
import an.dpr.manteniket.dao.BicisDAO;
import an.dpr.manteniket.dao.ComponentUsesDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

public class BikeCompListPage extends ManteniketPage {
    
    static final int ITEMS_PAGE = 5;
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(BikeCompListPage.class);
    public static final String BIKE = "bike.codBici";
    public static final String COMPONENT= "component.name";
    
    @SpringBean
    private ComponentUsesDAO dao;

    public BikeCompListPage(PageParameters params) throws StringValueConversionException, InstantiationException, IllegalAccessException, ClassNotFoundException {
	super();

	Long id = params.get(ID).toLongObject();
	Entity entity = params.get(ENTITY).toEnum(Entity.class);
	final PageParameters pp = new PageParameters();
	pp.add(ManteniketContracts.SOURCE_ID, id);
	pp.add(ManteniketContracts.ENTITY, entity);
	
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

	add(form);
	listado(id, entity);
    }
    
    private void listado(final Long id, final Entity entity) {
	//form
	//dataproducer
	//mantenikettable
	//columns
	//filter column
	//filter toolbar
    }
    
    
    private void listadoOld(final Long id, final Entity entity) {
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

class UsesSortDataProvider extends SortableDataProvider<ComponentUse, String> implements
	IFilterStateLocator<ComponentUse> {

    private static final Logger log = LoggerFactory.getLogger(UsesSortDataProvider.class);
    private ComponentUsesDAO dao;
    private static final long serialVersionUID = 1L;
    private List<ComponentUse> list;
    private ComponentUse filterState;
    private long size;
    private static final String DESCRIPCION = "descripcion";
    private static final String COD_BICI = "codBici";
    private static final String TIPO = "tipo";

    public UsesSortDataProvider(ComponentUsesDAO dao) {
	this.dao = dao;
	filterState = new ComponentUse();// importante, sino pegara un pete de null al
				 // intentar setear el filtro
    }

    @Override
    public Iterator<? extends ComponentUse> iterator(long first, long count) {
	log.debug("a iterar!");
	int fromPage = 0;
	if (first >= BikeCompListPage.ITEMS_PAGE) {
	    fromPage = ((int) (first / BikeCompListPage.ITEMS_PAGE));
	}
	list = getList(getSort(), fromPage, BikeCompListPage.ITEMS_PAGE);
	return list.iterator();
    }

    private List<ComponentUse> getList(SortParam<String> sortParam, int page,
	    int numberOfResults) {
	ComponentUse filtro = null;
	Sort sort = null;
	if (filterState != null && 
		(filterState.getBike() != null 
		|| filterState.getComponent()!=null) ) {
	    filtro = filterState;
	}
	
	return list;
    }

    @Override
    public long size() {
	return size;
    }

    private Sort defaultSort() {
	return new Sort(Sort.Direction.ASC, COD_BICI);
    }

    @Override
    public IModel<ComponentUse> model(ComponentUse object) {
	return Model.of(object);
    }

    public ComponentUse getFilterState() {
	log.debug("init");
	return filterState;
    }

    public void setFilterState(ComponentUse filterState) {
	log.debug("init");
	this.filterState = filterState;
    }

}