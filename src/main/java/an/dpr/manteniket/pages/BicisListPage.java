package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.GoAndClearFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import an.dpr.manteniket.bean.ManteniketContracts.Entity;
import an.dpr.manteniket.components.FontAwesomeIconTypeExt;
import an.dpr.manteniket.components.ManteniketDataTable;
import an.dpr.manteniket.components.ManteniketLinkColumn;
import an.dpr.manteniket.dao.BicisDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;

public class BicisListPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory
	    .getLogger(BicisListPage.class);
    public static final Long ITEMS_PAGE = new Long(3);
    @SpringBean
    private BicisDAO dao;

    public BicisListPage() {
	super();

	final BikeSortDataProvider dataProvider = new BikeSortDataProvider(dao);
//	// create the form used to contain all filter components
	final FilterForm<Bici> form = new FilterForm<Bici>("filter-form", dataProvider)
		{
	    private static final long serialVersionUID = 1L;
	    
	    @Override
	    protected void onSubmit()
	    {
		log.debug("submit filter form");
	    }
	};
	

	List<IColumn<Bici,String>> columns = new ArrayList<IColumn<Bici,String>>();
	columns.add(new PropertyColumn<Bici, String>(new ResourceModel("head.code"), "codBici", "codBici"));
	columns.add(new PropertyColumn<Bici, String>(new ResourceModel("head.desc"),"descripcion","descripcion"));

//	TextFilteredPropertyColumn<Bici, Bici, String> tpcTipo;
//	tpcTipo = new  TextFilteredPropertyColumn<Bici,Bici, String>(new ResourceModel("head.type"),"tipo","tipo"){
//
//	    private static final long serialVersionUID = 1L;
//	    @Override 
//	    public Component getFilter(String componentId, FilterForm<?> form){
//		TextFilter filter = (TextFilter)super.getFilter(componentId, form);
//		TextField txtFilter = filter.getFilter();
//		txtFilter.add(AttributeModifier.replace("class", "form-control"));
//		return filter;
//	    }
//	    
//	};
//	columns.add(tpcTipo);
	
	addActionColumns(columns);
	
	//TODO FilteredColumn
//	GoAndClearFilter gcFilter = new GoAndClearFilter("gcFilter", form, new ResourceModel("filter"), new ResourceModel("clear"));
//	form.add(gcFilter);
	
	ManteniketDataTable<Bici, String> table = new ManteniketDataTable<Bici, String>("table", columns,dataProvider, ITEMS_PAGE.intValue());
//	table.addTopToolbar(new FilterToolbar(table, form, dataProvider));
//	table.addBottomToolbar(new NavigationToolbar(table));
//	table.addBottomToolbar(new BootstrapNavigationToolbar(table)); necesitamos un <ul>!!
	table.add(new TableBehavior().striped());
	form.add(table);
	form.add(new BootstrapPagingNavigator("pagingNavigator", table));
	add(form);
	
	
	
	BootstrapForm buttonForm = new BootstrapForm("button-form");
	BootstrapButton btnAdd = new BootstrapButton("btnAdd", BTN_ADD){
	    
	    private static final long serialVersionUID = 1L;
	    
	    @Override
	    public void onSubmit(){
		setResponsePage(BicisPage.class);
	    }
	    
	};
	btnAdd.setLabel(new ResourceModel("btn.add"));
	buttonForm.add(btnAdd);
	add(buttonForm);
    }
    
    private void addActionColumns(List<IColumn<Bici, String>> columns) {
	// columns.add(createActionsColumn());
	IColumn<Bici, String> linkComponents = new ManteniketLinkColumn<Bici, BikeCompListPage, String>(
		Model.of(""), BikeCompListPage.class, Model.of(""),
		FontAwesomeIconType.cog, Entity.BIKE);
	columns.add(linkComponents);

	IColumn<Bici, String> linkEdit = new ManteniketLinkColumn<Bici, BicisPage, String>(Model.of(""),
		BicisPage.class, Model.of(""), FontAwesomeIconType.edit);
	columns.add(linkEdit);

	IColumn<Bici, String> linkDelete = new ManteniketLinkColumn<Bici, BicisDeletePage, String>(Model.of(""),
		BicisDeletePage.class, Model.of(""), FontAwesomeIconTypeExt.remove);
	columns.add(linkDelete);

    }

    /**
     * Create a composite column extending FilteredAbstractColumn. This column
     * adds a UserActionsPanel as its cell contents. It also provides the
     * go-and-clear filter control panel.
     */
    private FilteredAbstractColumn<Bici, String> createActionsColumn() {
	return new FilteredAbstractColumn<Bici, String>(Model.of("Actions")) {
	    private static final long serialVersionUID = 1L;

	    // return the go-and-clear filter for the filter toolbar
	    public Component getFilter(String componentId, FilterForm<?> form) {
		return new GoAndClearFilter(componentId, form, new ResourceModel("filter"), new ResourceModel("clear"));
	    }

	    // add the UserActionsPanel to the cell item
	    public void populateItem(Item<ICellPopulator<Bici>> cellItem, String componentId,
		    IModel<Bici> rowModel) {
//		Panel panel = new Panel(componentId, rowModel){
//		    private static final long serialVersionUID = 1L;
//		    
//		};
//		panel.add(ManteniketLink.createLink("editLink", rowModel,(Entity)null, 
//			BicisPage.class,Model.of(""), FontAwesomeIconType.edit));
//		panel.add(ManteniketLink.createLink("deleteLink", rowModel,(Entity)null, 
//			BicisDeletePage.class,Model.of(""), FontAwesomeIconTypeExt.remove));
//		
//		cellItem.add(panel);
		cellItem.add(new Label(componentId, "hola"));
	    }
	};
    }
}
	
class BikeSortDataProvider extends SortableDataProvider<Bici, String> implements IFilterStateLocator<Bici>{
    
    private static final Logger log = LoggerFactory.getLogger(BikeSortDataProvider.class);
    private BicisDAO dao;
    private static final long serialVersionUID = 1L;
    private List<Bici> list;
    private Bici filterState;
    private long size;
    private static final String DESCRIPCION= "descripcion";
    private static final String COD_BICI = "codBici";
    private static final String TIPO = "tipo";

    public BikeSortDataProvider(BicisDAO dao){
	this.dao = dao;
	filterState = new Bici();//importante, sino pegara un pete de null al intentar setear el filtro
    }

    @Override
    public Iterator<? extends Bici> iterator(long first, long count) {
	log.debug("a iterar!");
	int fromPage = 0;
	if (first >= BicisListPage.ITEMS_PAGE) {
	    fromPage = ((int) (first/BicisListPage.ITEMS_PAGE));
	}
	list = getList(getSort(), fromPage, BicisListPage.ITEMS_PAGE.intValue());
	return list.iterator();
    }

    private List<Bici> getList(SortParam<String> sortParam, int page, int numberOfResults) {
	Sort sort;
	if (sortParam != null && 
		(sortParam.getProperty().equals(COD_BICI)
			|| sortParam.getProperty().equals(TIPO)
			|| sortParam.getProperty().equals(DESCRIPCION)
			)
		){
	    //ordenacion por fecha
	    Direction direction;
	    if (sortParam.isAscending()){
		direction = Sort.Direction.ASC;
	    }else {
		direction = Sort.Direction.DESC;
	    }
	    sort = new Sort(direction, sortParam.getProperty());
	    if (filterState != null && filterState.getTipo()!=null){
		list = dao.findByTipo(filterState, sort, page, numberOfResults);
		size = dao.countByTipo(filterState, sort);
	    } else {
		list = dao.findAll(sort, page, numberOfResults);
		size = dao.countAll(sort);
	    }
	} else {
	    list = dao.findAll();
	    size = dao.countAll(null);
	}
	return list;
    }

    @Override
    public long size() {
	if (size == 0){
	    size = dao.countAll(null);
	}
	return size;
    }

    private void listadoPorDefecto() {
	Sort sort = new Sort(Sort.Direction.ASC, COD_BICI);
	setSort(COD_BICI, SortOrder.ASCENDING);
	list = dao.findAll(sort);
    }

    @Override
    public IModel<Bici> model(Bici object) {
	return Model.of(object);
    }

    public Bici getFilterState() {
	log.debug("init");
        return filterState;
    }

    public void setFilterState(Bici filterState) {
	log.debug("init");
        this.filterState = filterState;
    }
    
}

