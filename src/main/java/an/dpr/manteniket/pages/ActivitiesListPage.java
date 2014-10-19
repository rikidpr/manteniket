package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
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

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.components.LinkPanel;
import an.dpr.manteniket.components.ManteniketTable;
import an.dpr.manteniket.dao.ActivitiesDAO;
import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.template.ManteniketPage;

public class ActivitiesListPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ActivitiesListPage.class);
    @SpringBean
    private ActivitiesDAO dao;

    public ActivitiesListPage() {
	super();
	BootstrapForm form = new BootstrapForm("form");
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
	add(form);
	listado();
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void listado() {
	log.debug("iniciando listado");
	List<Activity> list = dao.findAll();
	ActivitySortData asd = new ActivitySortData(list);
	final DataView<Activity> dataView = new DataView<Activity>("rows", asd){

	    @Override
	    protected void populateItem(Item<Activity> item) {
		Activity act = item.getModelObject();
		RepeatingView rv = new RepeatingView("dataRow");
		rv.add(new Label(rv.newChildId(), new PropertyModel(item.getModel(), "date")));
		rv.add(new Label(rv.newChildId(), new PropertyModel(item.getModel(), "km")));
		rv.add(new Label(rv.newChildId(), new PropertyModel(item.getModel(), "bike.codBici")));
		PageParameters params = new PageParameters();
		params.set(ManteniketContracts.ID, act.getActivityId());
		rv.add(new LinkPanel(rv.newChildId(), params, ActivitiesPage.class, getString("btn.edit")));
		rv.add(new LinkPanel(rv.newChildId(), params, ActivityDeletePage.class, getString("btn.delete")));
		item.add(rv);
	    }
	    
	}; 
	dataView.setItemsPerPage(10);
	ManteniketTable table = new ManteniketTable("table");
	table.add(dataView);
	OrderByBorder dateSort = new OrderByBorder("orderByDate", "date", asd)
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSortChanged()
            {
                dataView.setCurrentPage(0);
            }
        };
	dateSort.add(new Label("headDate", new ResourceModel("head.date")));
	//ordenacion por fecha
	table.add(dateSort);

	OrderByBorder kmSort = new OrderByBorder("orderByKm", "km", asd)
	{
	    private static final long serialVersionUID = 1L;
	    
	    @Override
	    protected void onSortChanged()
	    {
		dataView.setCurrentPage(0);
	    }
	};
	kmSort.add(new Label("headKm", new ResourceModel("head.km")));
	table.add(kmSort);
	table.add(new Label("headBike", new ResourceModel("head.bike")));
	add(table);
	add(new BootstrapPagingNavigator("pagingNavigator", dataView));
    }


}

class ActivitySortData extends SortableDataProvider<Activity, String>{
    
    private static final long serialVersionUID = 1L;
    private List<Activity> list;
    private static final String DATE = "date";
    private static final String KM = "km";

    public ActivitySortData(List<Activity> list){
	this.list = list;
        setSort(DATE, SortOrder.DESCENDING);
    }

    @Override
    public Iterator<? extends Activity> iterator(long first, long count) {
	//FIXME mejora haciendo llamadas a DB de lo necesario en cuenta de traer todo el pastel y luego filtrar!!
	int fromIndex = new Long(first).intValue();
	int toIndex = (int) (fromIndex + count);
	List<Activity> actList = getList(getSort());
	return actList.subList(fromIndex, toIndex).iterator();
    }

    private List<Activity> getList(final SortParam<String> sort) {
	if (sort.getProperty().equals(DATE)){
	    //ordenacion por fecha
	    Collections.sort(list, new Comparator<Activity>() {

		@Override
		public int compare(Activity o1, Activity o2) {
		    int retValue = o1.getDate().compareTo(o2.getDate());
		    if (!sort.isAscending()){
			retValue *= -1;
		    }
		    return retValue;
		}
	    });
	} else if (sort.getProperty().equals(KM)){
	    //ordenacion por km
	    Collections.sort(list, new Comparator<Activity>(){

		@Override
		public int compare(Activity o1, Activity o2) {
		    int retValue = o1.getKm().compareTo(o2.getKm());
		    if (!sort.isAscending()){
			retValue *= -1;
		    }
		    return retValue;
		}});
	}
	return list;
    }

    @Override
    public long size() {
	return list.size();
    }

    @Override
    public IModel<Activity> model(Activity object) {
	return Model.of(object);
    }
    
};
