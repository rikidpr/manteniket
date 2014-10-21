package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeaderlessColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.components.LinkPanel;
import an.dpr.manteniket.components.ManteniketTable;
import an.dpr.manteniket.dao.BicisDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;

public class BicisListPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory
	    .getLogger(BicisListPage.class);
    public static final Long ITEMS_PAGE = new Long(3);
    @SpringBean
    private BicisDAO dao;

    public BicisListPage() {
	super();

	BootstrapForm form = new BootstrapForm("form");
	BootstrapButton btnAdd = new BootstrapButton("btnAdd", BTN_ADD){

	    private static final long serialVersionUID = 1L;
	    
	    @Override
	    public void onSubmit(){
		setResponsePage(BicisPage.class);
	    }
	
	};
	btnAdd.setLabel(new ResourceModel("btn.add"));
	form.add(btnAdd);
	add(form);
	listado();
    }

    private void listado() {
	listadoDataTable();
    }
    
    private void listadoDataTable() {
	final BikeSortDataProvider dataProvider = new BikeSortDataProvider(dao);

	List<IColumn<String,String>> columns = new ArrayList<IColumn<String,String>>();
	columns.add(new PropertyColumn<String, String>(new ResourceModel("head.code"), "codBici", "codBici"));
	columns.add(new PropertyColumn<String, String>(new ResourceModel("head.desc"),"descripcion","descripcion"));
//	columns.add(new PropertyColumn<String, String>(new ResourceModel("head.type"),"tipo","tipo"));
	TextFilteredPropertyColumn<String, String, String> tpcTipo;
	tpcTipo = new  TextFilteredPropertyColumn<String,String, String>(new ResourceModel("head.type"),"tipo","tipo");
//	FilterForm<?> form = new FilterForm<T>("filterForm", locator);
//	add(tpcTipo.getFilter("tipoFilter", form ));
	columns.add(tpcTipo);
	
	//TODO add links columns
	//TODO FilteredColumn
	
	DefaultDataTable table = new DefaultDataTable("table", columns,dataProvider, 10);
	table.add(new TableBehavior().striped());
	add(new BootstrapPagingNavigator("pagingNavigator", table));
	add(table);

    }
	
    private void listadoDataView(){
	log.debug("iniciando listado");
	List<Bici> bicis = dao.findAll();
	ListDataProvider<Bici> data = new ListDataProvider<Bici>(bicis);
	DataView<Bici> dataView = new DataView<Bici>("rows", data) {

	    @Override
	    protected void populateItem(Item<Bici> item) {
		Bici bici = item.getModelObject();
		RepeatingView rv = new RepeatingView("dataRow");
		rv.add(new Label(rv.newChildId(), new PropertyModel(item
			.getModel(), "codBici")));
		rv.add(new Label(rv.newChildId(), new PropertyModel(item
			.getModel(), "descripcion")));
		rv.add(new Label(rv.newChildId(), new PropertyModel(item
			.getModel(), "tipo")));
		PageParameters params = new PageParameters();
		params.add(ManteniketContracts.ID, bici.getIdBici());
		params.add(ManteniketContracts.ENTITY, ManteniketContracts.Entity.BIKE);
		params.add(ManteniketContracts.RETURN_PAGE, this.getClass().getName());
		rv.add(new LinkPanel(rv.newChildId(), params,
			BikeCompListPage.class, getString("btn.components")));
		rv.add(new LinkPanel(rv.newChildId(), params, BicisPage.class,
			getString("btn.edit")));
		rv.add(new LinkPanel(rv.newChildId(), params,
			BicisDeletePage.class, getString("btn.delete")));

		item.add(rv);
	    }

	};
	dataView.setItemsPerPage(3);
	ManteniketTable table = new ManteniketTable("table");
	table.add(dataView);
	add(table);
	add(new BootstrapPagingNavigator("pagingNavigator", dataView));
    }

}


class BikeSortDataProvider extends SortableDataProvider<Bici, String>{
    
    private BicisDAO dao;
    private static final long serialVersionUID = 1L;
    private List<Bici> list;
    private long size;
    private static final String DESCRIPCION= "descripcion";
    private static final String COD_BICI = "codBici";
    private static final String TIPO = "tipo";

    public BikeSortDataProvider(BicisDAO dao){
	this.dao = dao;
    }

    @Override
    public Iterator<? extends Bici> iterator(long first, long count) {
	int fromPage = 0;
	if (first >= BicisListPage.ITEMS_PAGE) {
	    fromPage = ((int) (first/BicisListPage.ITEMS_PAGE));
	}
	//FIXME mejora haciendo llamadas a DB de lo necesario en cuenta de traer todo el pastel y luego filtrar!!
	List<Bici> biciList = getList(getSort(), fromPage, BicisListPage.ITEMS_PAGE.intValue());
	return biciList.iterator();
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
	    list = dao.findAll(sort, page, numberOfResults);
	    
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
	Sort sort = new Sort(Sort.Direction.ASC, COD_BICI);
	setSort(COD_BICI, SortOrder.ASCENDING);
	list = dao.findAll(sort);
    }

    @Override
    public IModel<Bici> model(Bici object) {
	return Model.of(object);
    }
    
};

