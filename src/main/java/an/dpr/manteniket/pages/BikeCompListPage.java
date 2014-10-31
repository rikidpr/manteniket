
package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;
import static an.dpr.manteniket.bean.ManteniketContracts.ENTITY;
import static an.dpr.manteniket.bean.ManteniketContracts.ID;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValueConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.bean.ManteniketContracts.Entity;
import an.dpr.manteniket.components.ManteniketDataTable;
import an.dpr.manteniket.components.ManteniketLinkColumn;
import an.dpr.manteniket.dao.IComponentUsesDAO;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;

public class BikeCompListPage extends ManteniketPage {
    
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(BikeCompListPage.class);
    public static final int ITEMS_PAGE = 5;
    private static final String COMPONENT = "component.name";
    private static final String BIKE = "bike.codBici";
    private static final String INIT = "init";
    private static final String FINISH = "finish";
    
    @SpringBean
    private IComponentUsesDAO dao;

    public BikeCompListPage(PageParameters params) throws StringValueConversionException, 
    		InstantiationException, IllegalAccessException, ClassNotFoundException {
	super();

	Long id = params.get(ID).toLongObject();
	Entity entity = params.get(ENTITY).toEnum(Entity.class);
	final PageParameters pp = new PageParameters();
	pp.add(ManteniketContracts.SOURCE_ID, id);
	pp.add(ManteniketContracts.ENTITY, entity);
	
	BootstrapForm form = new BootstrapForm("button-form");
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
	BikeCompDataProvider dataProvider = new BikeCompDataProvider(dao);
	FilterForm form = new FilterForm("filter-form", dataProvider);
	List<IColumn<ComponentUse, String>> columns = new ArrayList<IColumn<ComponentUse, String>>();
	columns.add(new PropertyColumn<ComponentUse, String>(new ResourceModel("head.bike"), BIKE, BIKE));
	columns.add(new PropertyColumn<ComponentUse, String>(new ResourceModel("head.component"), COMPONENT, COMPONENT));
	columns.add(new PropertyColumn<ComponentUse, String>(new ResourceModel("head.init"), INIT, INIT));
	columns.add(new PropertyColumn<ComponentUse, String>(new ResourceModel("head.finish"), FINISH, FINISH));
	
	addActionColumns(columns, id, entity);
	
	DataTable<ComponentUse, String> table = new ManteniketDataTable<ComponentUse, String>("table", columns, dataProvider, ITEMS_PAGE);
	//TODO add toolbar filter
	form.add(table);
	add(form);
    }
    
    private void addActionColumns(List<IColumn<ComponentUse, String>> columns, Long id, Entity entity) {
	PageParameters params = new PageParameters();
	params.add(ManteniketContracts.SOURCE_ID,  id);
	params.add(ManteniketContracts.ENTITY,  entity);
  	IColumn<ComponentUse, String> linkEdit = new ManteniketLinkColumn<ComponentUse, ComponentUsePage, String>(Model.of(""),
  		ComponentUsePage.class, Model.of(""), FontAwesomeIconType.edit,params);
  	columns.add(linkEdit);
      }

}

class BikeCompDataProvider extends SortableDataProvider<ComponentUse, String> implements IFilterStateLocator<ComponentUse> {
    
    private static final long serialVersionUID = 642969964923785080L;
    
    private ComponentUse filterState;
    private IComponentUsesDAO dao;
    private List<ComponentUse> list;
    
    BikeCompDataProvider(IComponentUsesDAO dao){
	this.dao = dao;
	this.filterState = new ComponentUse();
    }

    @Override
    public Iterator<? extends ComponentUse> iterator(long first, long count) {
	int fromPage = 0;
	if (first >= BikeCompListPage.ITEMS_PAGE) {
	    fromPage = ((int) (first/BikeCompListPage.ITEMS_PAGE));
	}
	//FIXME mejora haciendo llamadas a DB de lo necesario en cuenta de traer todo el pastel y luego filtrar!!
	list= getList(getSort(), fromPage, BikeCompListPage.ITEMS_PAGE);
	return list.iterator();
    }

    private List<ComponentUse> getList(SortParam<String> sortParam, int fromPage, int itemsPage) {
	ComponentUse filtro = null;
	Sort sort = null;
	if (filterState != null && 
		(filterState.getBike()!= null || filterState.getComponent()!=null)
		){
	    filtro = filterState;
	}
	//TODO DEFINIR SORT
	sort = defaultSort();
	list = dao.find(filtro, sort, fromPage, itemsPage);
	return list;
    }

    private Sort defaultSort() {
	return new Sort(Direction.ASC,"init");
    }

    @Override
    public long size() {
	ComponentUse filtro = null;
	if (filterState != null && 
		(filterState.getBike()!= null || filterState.getComponent()!=null)
		){
	    filtro = filterState;
	}
	return dao.count(filtro);
    }

    @Override
    public IModel<ComponentUse> model(ComponentUse object) {
	return Model.of(object);
    }

    @Override
    public ComponentUse getFilterState() {
	return filterState;
    }

    @Override
    public void setFilterState(ComponentUse state) {
	filterState = state;
    }
    
}
