package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import an.dpr.manteniket.bean.ManteniketBean;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.bean.ManteniketContracts.Entity;
import an.dpr.manteniket.components.FontAwesomeIconTypeExt;
import an.dpr.manteniket.components.LinkPanel;
import an.dpr.manteniket.components.ManteniketLinkColumn;
import an.dpr.manteniket.components.ManteniketTable;
import an.dpr.manteniket.dao.ActivitiesDAO;
import an.dpr.manteniket.dao.BicisDAO;
import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.template.ManteniketPage;

public class ActivitiesListPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ActivitiesListPage.class);
    public static final Long ITEMS_PAGE = new Long(5);
    @SpringBean
    private ActivitiesDAO dao;
    @SpringBean
    private BicisDAO bicisDao;

    public ActivitiesListPage() {
	super();
	initComponents();
    }
    
    private void initComponents(){
//	BootstrapForm form = new BootstrapForm("form");
	final ActivitySortData dataProvider = new ActivitySortData(dao);
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

	List<IColumn<? extends ManteniketBean,String>> columns = new ArrayList<IColumn<? extends ManteniketBean,String>>();
	columns.add(new PropertyColumn<Activity, String>(new ResourceModel("head.date"), "date", "date"));
	columns.add(new PropertyColumn<Activity, String>(new ResourceModel("head.km"),"km","km"));	
	//	columns.add(new PropertyColumn<Activity, String>(new ResourceModel("head.bike"),"bike.codBici","bike.codBici"));

	List<Bici> list = new ArrayList<Bici>();
	list.add(bicisDao.findByCodBici("Trek 3700"));
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
	
	
	DefaultDataTable table = new DefaultDataTable("table", columns,dataProvider, 10);
	table.add(new TableBehavior().striped());
	form.add(table);
	form.add(new BootstrapPagingNavigator("pagingNavigator", table));

	add(form);
    }
    

}

class ActivitySortData extends SortableDataProvider<Activity, String> implements IFilterStateLocator<Activity>{
    
    private static final Logger log = LoggerFactory.getLogger(ActivitySortData.class);
    private ActivitiesDAO dao;
    private static final long serialVersionUID = 1L;
    private static final String DATE = "date";
    private static final String KM = "km";
    private List<Activity> list;
    private long size;
    private Activity filterState;

    public ActivitySortData(ActivitiesDAO dao){
	this.dao = dao;
    }

    @Override
    public Iterator<? extends Activity> iterator(long first, long count) {
	int fromPage = 0;
	if (first >= ActivitiesListPage.ITEMS_PAGE) {
	    fromPage = ((int) (first/ActivitiesListPage.ITEMS_PAGE));
	}
	//FIXME mejora haciendo llamadas a DB de lo necesario en cuenta de traer todo el pastel y luego filtrar!!
	List<Activity> actList = getList(getSort(), fromPage, ActivitiesListPage.ITEMS_PAGE.intValue());
	return filtra(actList);
    }

    //filtra por el filterState indicado
    private Iterator<? extends Activity> filtra(List<Activity> actList) {
	Iterator<? extends Activity> iterator;
	if (filterState == null){
	    iterator = actList.iterator();
	} else {
	    log.debug("filtramoooos"+filterState);
	    iterator = actList.iterator();
//	    List<Activity> newList = new ArrayList<Activity>();
//	    for(Activity act : actList){
//		if (act.getBike().equals(filterState)){
//		    newList.add(act);
//		}
//	    }
//	    iterator = newList.iterator();
	}
	return iterator;
    }

    private List<Activity> getList(SortParam<String> sortParam, int page, int numberOfResults) {
	Sort sort;
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
	    list = dao.findAll(sort, page, numberOfResults);
	    
	}
	return list;
    }

    private List<Activity> getList(final SortParam<String> sortParam) {
	Sort sort;
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
	    list = dao.findAll(sort);
	    
	}
	return list;
    }

    @Override
    public long size() {
	if (list == null){
	    listadoPorDefecto();
	    size = list.size();
	}
	return size;
    }

    private void listadoPorDefecto() {
	Sort sort = new Sort(Sort.Direction.ASC, DATE);
	setSort(DATE, SortOrder.ASCENDING);
	list = dao.findAll(sort);
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
