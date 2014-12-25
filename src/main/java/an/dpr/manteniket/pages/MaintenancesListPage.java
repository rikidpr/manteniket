package an.dpr.manteniket.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.components.ManteniketDataTable;
import an.dpr.manteniket.components.ManteniketLinkColumn;
import an.dpr.manteniket.dao.IMaintenanceDAO;
import an.dpr.manteniket.dao.IUserDAO;
import an.dpr.manteniket.domain.Maintenance;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;

/**
 * 
 * @author saez
 *
 */
public class MaintenancesListPage extends ManteniketPage{

    private static final long serialVersionUID = -7993237984638093168L;
    private static final Logger log = LoggerFactory.getLogger(MaintenancesListPage.class);
    public static final int ITEMS_PAGE = 10;
    static final String COMPONENT = "component.name";
    static final String DATE = "date";
    static final String TYPE = "type";
    
    @SpringBean
    private IMaintenanceDAO mDao;
    @SpringBean
    private IUserDAO userDao;
    
    public MaintenancesListPage(){
	this(null);
    }
    
    public MaintenancesListPage(PageParameters params){
	super();
	Maintenance bean = new Maintenance();
	bean.setUser(getUser());
	
	final MaintenanceSortDataProvider dataProvider = new MaintenanceSortDataProvider(mDao, bean);
	final FilterForm<Maintenance> form = new FilterForm<Maintenance>("filter-form", dataProvider) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void onSubmit() {
		log.debug("submit filter form");
	    }
	};
	
	List<IColumn<Maintenance, String>> columns = new ArrayList<IColumn<Maintenance, String>>();
	columns.add(new PropertyColumn<Maintenance, String>(new ResourceModel("head.date"), DATE, DATE));
	columns.add(new PropertyColumn<Maintenance, String>(new ResourceModel("head.component"), COMPONENT, COMPONENT));
	columns.add(new PropertyColumn<Maintenance, String>(new ResourceModel("head.type"), TYPE, TYPE));
	
	addActionColumns(columns);
	
	ManteniketDataTable<Maintenance, String> table = new ManteniketDataTable<Maintenance, String>
		("table", columns, dataProvider, ITEMS_PAGE);
	form.add(table);
	add(form);
	
	//btn add
	BootstrapForm btnForm = new BootstrapForm("button-form");
	BootstrapButton btnAdd = new BootstrapButton("btnAdd", ManteniketContracts.BTN_ADD){

	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onSubmit() {
		setResponsePage(MaintenancesPage.class);
	    }

	};
	btnAdd.setLabel(new ResourceModel("btn.add"));
	btnForm.add(btnAdd);
	add(btnForm);
    }

    private void addActionColumns(List<IColumn<Maintenance, String>> columns) {
	columns.add(new ManteniketLinkColumn<Maintenance, MaintenancesPage, String>(Model.of(""), 
		MaintenancesPage.class, Model.of(""), FontAwesomeIconType.edit));
//	TODO DELETE PAGE columns.add(new ManteniketLinkColumn<Maintenance, MaintenancesListPage, String>(Model.of(""), 
//		MaintenancesRemovePage.class,Model.of(""), FontAwesomeIconType.remove));
    }
    
    //columns
    //filters

}

class MaintenanceSortDataProvider extends SortableDataProvider<Maintenance, String> implements IFilterStateLocator<Maintenance> {
    
    private IMaintenanceDAO dao;
    private static final long serialVersionUID = 1L;
    private List<Maintenance> list;
    private Maintenance filterState;
    private static final String[] filtros={MaintenancesListPage.COMPONENT, 
	MaintenancesListPage.DATE, MaintenancesListPage.TYPE};

    public MaintenanceSortDataProvider(IMaintenanceDAO dao, Maintenance filterBike) {
	this.dao = dao;
	filterState = filterBike;
    }

    @Override
    public Iterator<? extends Maintenance> iterator(long first, long count) {
	int fromPage = 0;
	if (first >= MaintenancesListPage.ITEMS_PAGE) {
	    fromPage = ((int) (first / BicisListPage.ITEMS_PAGE));
	}
	list = getList(getSort(), fromPage, MaintenancesListPage.ITEMS_PAGE);
	return list.iterator();
    }

    private List<Maintenance> getList(SortParam<String> sortParam, int fromPage, int itemsPage) {
	Sort sort = null;
	if (tieneFiltro(sortParam)) {
	    Direction direction;
	    if (sortParam.isAscending()) {
		direction = Sort.Direction.ASC;
	    } else {
		direction = Sort.Direction.DESC;
	    }
	    sort = new Sort(direction, sortParam.getProperty());
	} else {
	    sort = defaultSort();
	}
	list = dao.find(filterState, sort, fromPage, itemsPage);
	return list;
    }

    private boolean tieneFiltro(SortParam<String> sortParam) {
	boolean tiene = false;
	if (sortParam != null){
	    for(String filtro : filtros){
		if (filtro.equals(sortParam.getProperty())){
		    tiene =  true;
		    break;
		}
	    }
	}
	return tiene;
    }

    @Override
    public long size() {
	return dao.count(filterState);
    }

    private Sort defaultSort() {
	return new Sort(Sort.Direction.DESC, MaintenancesListPage.DATE);
    }

    @Override
    public IModel<Maintenance> model(Maintenance object) {
	return Model.of(object);
    }
    

    @Override
    public Maintenance getFilterState() {
	return filterState;
    }

    @Override
    public void setFilterState(Maintenance state) {
	this.filterState = state;
    }
    
}
