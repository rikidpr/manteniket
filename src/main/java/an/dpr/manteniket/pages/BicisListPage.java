package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import an.dpr.manteniket.bean.CyclingType;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.bean.ManteniketContracts.Entity;
import an.dpr.manteniket.components.FontAwesomeIconTypeExt;
import an.dpr.manteniket.components.ManteniketDataTable;
import an.dpr.manteniket.components.ManteniketLinkColumn;
import an.dpr.manteniket.dao.IBikesDAO;
import an.dpr.manteniket.dao.IUserDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.User;
import an.dpr.manteniket.security.AppSecurity;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;

public class BicisListPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory
	    .getLogger(BicisListPage.class);
    public static final Long ITEMS_PAGE = new Long(3);
    @SpringBean
    private IBikesDAO dao;
    @SpringBean
    private IUserDAO userDao;

    public BicisListPage() {
	super();

	Bici filterBike = new Bici();
	filterBike.setUser(getUser());
	final BikeSortDataProvider dataProvider = new BikeSortDataProvider(dao, filterBike);
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
	IModel<List<? extends String>> cmb = Model.ofList(Arrays.asList(CyclingType.names())); 
	columns.add( new ChoiceFilteredPropertyColumn<Bici, String, String>( new ResourceModel( "head.type" ), "tipo", "tipo", cmb));
	
	addActionColumns(columns);
	
	ManteniketDataTable<Bici, String> table = new ManteniketDataTable<Bici, String>("table", columns,dataProvider, ITEMS_PAGE.intValue());
	table.addTopToolbar(new FilterToolbar(table, form, dataProvider));
	form.add(table);
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
	
    private Bici newBiciInstance() {
	Bici bici = new Bici();
	//TODO esto del user a ManteniketApp
	User user = (User) getSession().getAttribute(ManteniketContracts.LOGGED_USER);
	if (user == null){
	    user = userDao.getUser(AppSecurity.getUserName());
	    getSession().setAttribute(ManteniketContracts.LOGGED_USER, user);
	}
	bici.setUser(user);
	return bici;
    }

    private void addActionColumns(List<IColumn<Bici, String>> columns) {
	PageParameters params = new PageParameters();
	params.add(ManteniketContracts.ENTITY,  Entity.BIKE);
  	IColumn<Bici, String> linkComponents = new ManteniketLinkColumn<Bici, BikeCompListPage, String>(
  		Model.of(""), BikeCompListPage.class, Model.of(""),
  		FontAwesomeIconType.cog,params);
  	columns.add(linkComponents);

  	IColumn<Bici, String> linkEdit = new ManteniketLinkColumn<Bici, BicisPage, String>(Model.of(""),
  		BicisPage.class, Model.of(""), FontAwesomeIconType.edit);
  	columns.add(linkEdit);

  	IColumn<Bici, String> linkDelete = new ManteniketLinkColumn<Bici, BicisDeletePage, String>(Model.of(""),
  		BicisDeletePage.class, Model.of(""), FontAwesomeIconTypeExt.remove);
  	columns.add(linkDelete);

      }
}


class BikeSortDataProvider extends SortableDataProvider<Bici, String> implements IFilterStateLocator<Bici>{
    
    private IBikesDAO dao;
    private static final long serialVersionUID = 1L;
    private List<Bici> list;
    private long size;
    private Bici filterState;
    private static final String DESCRIPCION = "descripcion";
    private static final String COD_BICI = "codBici";
    private static final String TIPO = "tipo"; 

    public BikeSortDataProvider(IBikesDAO dao, Bici filterBike){
	this.dao = dao;
	filterState = filterBike;
    }

    @Override
    public Iterator<? extends Bici> iterator(long first, long count) {
	int fromPage = 0;
	if (first >= BicisListPage.ITEMS_PAGE) {
	    fromPage = ((int) (first/BicisListPage.ITEMS_PAGE));
	}
	//FIXME mejora haciendo llamadas a DB de lo necesario en cuenta de traer todo el pastel y luego filtrar!!
	list= getList(getSort(), fromPage, BicisListPage.ITEMS_PAGE.intValue());
	return list.iterator();
    }

    private List<Bici> getList(SortParam<String> sortParam, int page, int numberOfResults) {
	Sort sort = null;
	if (sortParam != null && 
		(sortParam.getProperty().equals(COD_BICI)
			|| sortParam.getProperty().equals(TIPO)
			|| sortParam.getProperty().equals(DESCRIPCION)
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
	list = dao.find(filterState, sort, page, numberOfResults);
	return list;
    }

    @Override
    public long size() {
	Bici filtro = null;
	if (filterState != null && filterState.getTipo() != null)
	    filtro = filterState;
	size = dao.count(filtro);
	return size;
    }

    private Sort defaultSort() {
	return new Sort(Sort.Direction.ASC, COD_BICI);
    }

    @Override
    public IModel<Bici> model(Bici object) {
	return Model.of(object);
    }

    public Bici getFilterState() {
        return filterState;
    }

    public void setFilterState(Bici filterState) {
        this.filterState = filterState;
    }
    
};

