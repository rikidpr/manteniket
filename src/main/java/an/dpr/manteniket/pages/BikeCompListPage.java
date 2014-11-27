
package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;
import static an.dpr.manteniket.bean.ManteniketContracts.ENTITY;
import static an.dpr.manteniket.bean.ManteniketContracts.ID;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
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
import an.dpr.manteniket.components.DatePropertyColumn;
import an.dpr.manteniket.components.ManteniketDataTable;
import an.dpr.manteniket.components.ManteniketLinkColumn;
import an.dpr.manteniket.components.converter.ManteniketDateConverter;
import an.dpr.manteniket.dao.ComponentesDAO;
import an.dpr.manteniket.dao.IBikesDAO;
import an.dpr.manteniket.dao.IComponentUsesDAO;
import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;


public class BikeCompListPage extends ManteniketPage {
    
    static final int ITEMS_PAGE = 5;
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(BikeCompListPage.class);
    static final String COMPONENT = "component.name";
    static final String BIKE = "bike.codBici";
    static final String INIT = "init";
    static final String FINISH = "finish";
    
    @SpringBean
    private IComponentUsesDAO dao;
    @SpringBean
    private IBikesDAO bikesDao;
    @SpringBean
    private ComponentesDAO compDao;

    public BikeCompListPage(PageParameters params) throws StringValueConversionException, 
    		InstantiationException, IllegalAccessException, ClassNotFoundException {
	super();

	Long id = params.get(ID).isNull() ? null : params.get(ID).toLongObject();
	Entity entity = params.get(ENTITY).isNull() ? null : params.get(ENTITY).toEnum(Entity.class);
	final PageParameters pp = new PageParameters();
	if (id != null){
	    pp.add(ManteniketContracts.SOURCE_ID, id);
	    pp.add(ManteniketContracts.ENTITY, entity);
	}
	
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
	ComponentUse cu = new ComponentUse();
	cu.setUser(getUser());
	BikeCompDataProvider dataProvider = new BikeCompDataProvider(dao, cu);
	FilterForm<ComponentUse> form = new FilterForm<ComponentUse>("filter-form", dataProvider);
	List<IColumn<ComponentUse, String>> columns = new ArrayList<IColumn<ComponentUse, String>>();

	//	columns.add(new PropertyColumn<ComponentUse, String>(new ResourceModel("head.bike"), BIKE, BIKE));
	columns.add( new ChoiceFilteredPropertyColumn<ComponentUse, String, String>( 
		new ResourceModel( "head.bike" ),BIKE,BIKE, getCmbBikes()));

	//	columns.add(new PropertyColumn<ComponentUse, String>(new ResourceModel("head.component"), COMPONENT, COMPONENT));
	columns.add(new ChoiceFilteredPropertyColumn<ComponentUse, String, String>(
		new ResourceModel("head.component"), COMPONENT, COMPONENT, getCmbComponents()));
	
	DateConverter dateConverter = ManteniketDateConverter.getInstance();
	columns.add(new DatePropertyColumn<ComponentUse, String>(new ResourceModel("head.init"), INIT, INIT, dateConverter ));
	columns.add(new DatePropertyColumn<ComponentUse, String>(new ResourceModel("head.finish"), FINISH, FINISH, dateConverter ));
	
	addActionColumns(columns, id, entity);
	
	DataTable<ComponentUse, String> table = new ManteniketDataTable<ComponentUse, String>("table", columns, dataProvider, ITEMS_PAGE);
	table.addTopToolbar(new FilterToolbar(table, form, dataProvider));
	form.add(table);
	add(form);
    }
    
    private IModel<List<? extends String>> getCmbBikes() {
	List<String> bikeCodsList = new ArrayList<String>();
	List<Bici> bikes = bikesDao.findAll(getUser());
	for(Bici bike:bikes){
	    bikeCodsList.add(bike.getCodBici());
	}
	return Model.ofList(bikeCodsList);
    }
    
    private IModel<List<? extends String>> getCmbComponents(){
	List<String> list = new ArrayList<String>();
	List<Component> comps = compDao.findAll();
	for(Component comp:comps)
	    list.add(comp.getName());
	return Model.ofList(list);
    }


    private void addActionColumns(List<IColumn<ComponentUse, String>> columns, Long id, Entity entity) {
	PageParameters params = new PageParameters();
	if (id!=null){
	    params.add(ManteniketContracts.SOURCE_ID,  id);
	    params.add(ManteniketContracts.ENTITY,  entity);
	}
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
    
    BikeCompDataProvider(IComponentUsesDAO dao, ComponentUse componentUse){
	this.dao = dao;
	this.filterState = componentUse;
	
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
	ComponentUse filtro = getFiltro();
	Sort sort = null;
	if (sortParam != null && 
		(sortParam.getProperty().equals(BikeCompListPage.COMPONENT)
			|| sortParam.getProperty().equals(BikeCompListPage.BIKE)
			|| sortParam.getProperty().equals(BikeCompListPage.INIT)
			|| sortParam.getProperty().equals(BikeCompListPage.FINISH)
			)
		){
	    Direction direction;
	    if (sortParam.isAscending()){
		direction = Sort.Direction.ASC;
	    }else {
		direction = Sort.Direction.DESC;
	    }
	    sort = new Sort(direction, sortParam.getProperty());
	} else {
	    sort = defaultSort();
	}
	list = dao.find(filtro, sort, fromPage, itemsPage);
	return list;
    }

    private Sort defaultSort() {
	return new Sort(Direction.DESC,"init");
    }

    @Override
    public long size() {
	ComponentUse filtro = getFiltro();
	return dao.count(filtro);
    }
    
    private ComponentUse getFiltro(){
	ComponentUse filtro = null; 
	if (filterState != null){
	    Bici bike = filterState.getBike();
	    Component comp = filterState.getComponent();
	    if ((bike!= null && bike.getCodBici()!=null && !bike.getCodBici().isEmpty())
		    ||
		    (comp!=null && comp.getName()!= null && !comp.getName().isEmpty())){
		filtro = filterState;
	    }
	}
	return filtro;
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
