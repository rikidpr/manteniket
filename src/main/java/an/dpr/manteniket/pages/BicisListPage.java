package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
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
import an.dpr.manteniket.dao.IBikesDAO;
import an.dpr.manteniket.domain.Bici;
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
	final BikeSortDataProvider dataProvider = new BikeSortDataProvider(dao);

	List<IColumn<Bici,String>> columns = new ArrayList<IColumn<Bici,String>>();
	columns.add(new PropertyColumn<Bici, String>(new ResourceModel("head.code"), "codBici", "codBici"));
	columns.add(new PropertyColumn<Bici, String>(new ResourceModel("head.desc"),"descripcion","descripcion"));
	addActionColumns(columns);
	
	DataTable<Bici, String> table = new ManteniketDataTable<Bici, String>("table", columns,dataProvider, ITEMS_PAGE.intValue());
	add(table);

    }
	
    private void addActionColumns(List<IColumn<Bici, String>> columns) {
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
}


class BikeSortDataProvider extends SortableDataProvider<Bici, String> {
    
    private IBikesDAO dao;
    private static final long serialVersionUID = 1L;
    private List<Bici> list;
    private long size;
    private Bici filterState;
    private static final String DESCRIPCION = "descripcion";
    private static final String COD_BICI = "codBici";
    private static final String TIPO = "tipo";

    public BikeSortDataProvider(IBikesDAO dao){
	this.dao = dao;
	filterState = new Bici();
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
	Sort sort;
	//TODO PUEDE SER SORT NULL PERO QUE ESTEMOS PAGINANDO!!! 
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
	    sort = getDefaultSort();
	}
	list = dao.findAll(sort, page, numberOfResults);
	size = dao.count(filterState);
	return list;
    }

    @Override
    public long size() {
	if (list == null){
	    size = dao.count();
	}
	return size;
    }

    private void listadoPorDefecto() {
	Sort sort = getDefaultSort();
	setSort(COD_BICI, SortOrder.ASCENDING);
	list = dao.findAll(sort);
    }

    private Sort getDefaultSort() {
	Sort sort = new Sort(Sort.Direction.ASC, COD_BICI);
	return sort;
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

