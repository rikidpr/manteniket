package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
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
import an.dpr.manteniket.components.FontAwesomeIconTypeExt;
import an.dpr.manteniket.components.ManteniketDataTable;
import an.dpr.manteniket.components.ManteniketLinkColumn;
import an.dpr.manteniket.dao.IActivityDao;
import an.dpr.manteniket.dao.IBikesDAO;
import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;

public class ActivitiesListPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ActivitiesListPage.class);
    public static final Long ITEMS_PAGE = new Long(5);
    @SpringBean
    private IActivityDao dao;
    @SpringBean
    private IBikesDAO bicisDao;

    public ActivitiesListPage() {
	super();
	initComponents();
    }
    
    private void initComponents(){
//	BootstrapForm form = new BootstrapForm("form");
	Activity filtroBase=new Activity();
	filtroBase.setUser(getUser());
	final ActivitySortData dataProvider = new ActivitySortData(dao, filtroBase);
	FilterForm<Activity> form = new FilterForm<Activity>("form", dataProvider);
	BootstrapButton btnAdd = new BootstrapButton("btnAdd", BTN_ADD){

	    private static final long serialVersionUID = 1L;
	    
	    @Override
	    public void onSubmit(){
		PageParameters params = new PageParameters();
		params.set(ManteniketContracts.ID, 0);
		setResponsePage(ActivitiesPage.class);
	    }
	
	};
	btnAdd.setLabel(new ResourceModel("btn.add"));
	form.add(btnAdd);

	log.debug("inicio");

	List<IColumn<Activity,String>> columns = new ArrayList<IColumn<Activity,String>>();
	columns.add(new PropertyColumn<Activity, String>(new ResourceModel("head.date"), "date", "date"));
	columns.add(new PropertyColumn<Activity, String>(new ResourceModel("head.km"),"km","km"));	
	//	columns.add(new PropertyColumn<Activity, String>(new ResourceModel("head.bike"),"bike.codBici","bike.codBici"));

	List<Bici> list = bicisDao.findAll(getUser());
	IModel<List<? extends Bici>> filterChoices = Model.ofList(list);
	ChoiceFilteredPropertyColumn cfpc = new ChoiceFilteredPropertyColumn<Activity, Bici, String>
		(new ResourceModel("head.bike"),"bike.codBici","bike.codBici", filterChoices);
	columns.add(cfpc);
	
	//links
	IColumn<Activity,String> linkEdit= new ManteniketLinkColumn<Activity, ActivitiesPage, String>
		(Model.of(""), ActivitiesPage.class, Model.of(""), FontAwesomeIconType.edit);
	columns.add(linkEdit);
	
	IColumn<Activity,String> linkDelete= new ManteniketLinkColumn<Activity, ActivityDeletePage, String>
		(Model.of(""), ActivityDeletePage.class, Model.of(""), FontAwesomeIconTypeExt.remove);
	columns.add(linkDelete);
	
	
	ManteniketDataTable<Activity, String> table = new ManteniketDataTable<Activity, String>("table", columns,dataProvider, 10);
	table.add(new TableBehavior().striped());
	form.add(table);

	add(form);
    }
    

}

class ActivitySortData extends SortableDataProvider<Activity, String> implements IFilterStateLocator<Activity>{
    
    private static final Logger log = LoggerFactory.getLogger(ActivitySortData.class);
    private IActivityDao dao;
    private static final long serialVersionUID = 1L;
    private static final String DATE = "date";
    private static final String KM = "km";
    private Activity filterState;

    public ActivitySortData(IActivityDao dao, Activity filtroBase){
	this.dao = dao;
	filterState = filtroBase;
    }

    @Override
    public Iterator<? extends Activity> iterator(long first, long count) {
	int fromPage = 0;
	if (first >= ActivitiesListPage.ITEMS_PAGE) {
	    fromPage = ((int) (first/ActivitiesListPage.ITEMS_PAGE));
	}
	List<Activity> list = getList(getSort(), fromPage, ActivitiesListPage.ITEMS_PAGE.intValue());
	return list.iterator();
    }

    private List<Activity> getList(SortParam<String> sortParam, int fromPage, int numberOfResults) {
	Sort sort = getSort(sortParam);
	return dao.find(filterState, sort, fromPage, numberOfResults);
    }
    
    private Sort getSort(SortParam<String> sortParam){
	Sort sort ;
	if (sortParam != null && 
		(sortParam.getProperty().equals(DATE)
			|| sortParam.getProperty().equals(KM))
		){
	    //ordenacion por fecha
	    Direction direction;
	    if (sortParam.isAscending()){
		direction = Sort.Direction.ASC;
	    }else {
		direction = Sort.Direction.DESC;
	    }
	    sort = new Sort(direction, sortParam.getProperty());
	} else{
	    sort = new Sort(Sort.Direction.DESC, DATE);
	}
	return sort;
    }

    public long size() {
	return dao.count(filterState);
    }

    @Override
    public IModel<Activity> model(Activity object) {
	return Model.of(object);
    }

    @Override
    public Activity getFilterState() {
	return filterState;
    }

    @Override
    public void setFilterState(Activity state) {
	filterState = state;
    }
    
};
