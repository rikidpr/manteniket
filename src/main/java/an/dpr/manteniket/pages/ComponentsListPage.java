package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.BTN_ADD;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
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
import an.dpr.manteniket.bean.ManteniketContracts.Entity;
import an.dpr.manteniket.components.FontAwesomeIconTypeExt;
import an.dpr.manteniket.components.LinkPanel;
import an.dpr.manteniket.components.ManteniketDataTable;
import an.dpr.manteniket.components.ManteniketLinkColumn;
import an.dpr.manteniket.components.ManteniketTable;
import an.dpr.manteniket.dao.ComponentesDAO;
import an.dpr.manteniket.dao.IActivityDao;
import an.dpr.manteniket.dao.IComponentsDAO;
import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;

public class ComponentsListPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ComponentsListPage.class);
    static final String NAME = "name";
    static final String TYPE = "type";
    static final String KM = "km";
    private static final int ITEMS_PAGE = 5;
    @SpringBean
    private ComponentesDAO dao;
    @SpringBean
    private IActivityDao actDao;

    public ComponentsListPage() {
	super();
	addBotonera();
	addListado();
    }

    private void addBotonera() {
	BootstrapForm form = new BootstrapForm("buttons-form");
	BootstrapButton btnAdd = new BootstrapButton("btnAdd", BTN_ADD) {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onSubmit() {
		setResponsePage(ComponentsPage.class);
	    }

	};
	btnAdd.setLabel(new ResourceModel("btn.add"));
	form.add(btnAdd);
	add(form);
    }

    private void addListado() {
	log.debug("iniciando listado");
	Component comp = new Component();
	comp.setUser(getUser());
	ComponentSortDataProvider dataProvider = new ComponentSortDataProvider(dao, comp);
	FilterForm<Component> form = new FilterForm<Component>("filter-form", dataProvider);

	// add coolumns
	List<IColumn<Component, String>> columns = new ArrayList<IColumn<Component, String>>();
	columns.addAll(getColumns());
	columns.addAll(getActionColumns());
	// add filter columns, filter form, toolbar form
	ManteniketDataTable<Component, String> table = new ManteniketDataTable<Component, String>
		("table", columns, dataProvider, ITEMS_PAGE);
	form.add(table);
	add(form);

    }

    private Collection<? extends IColumn<Component, String>> getActionColumns() {
	List<IColumn<Component, String>> columns = new ArrayList<IColumn<Component, String>>();
	PageParameters params = new PageParameters();
	params.add(ManteniketContracts.ENTITY, Entity.COMPONENT);
	IColumn<Component, String> linkComponents = new ManteniketLinkColumn<Component, BikeCompListPage, String>(Model.of(""),
		BikeCompListPage.class, Model.of(""), FontAwesomeIconType.cog, params);
	columns.add(linkComponents);

	IColumn<Component, String> linkEdit = new ManteniketLinkColumn<Component, ComponentsPage, String>(Model.of(""),
		ComponentsPage.class, Model.of(""), FontAwesomeIconType.edit);
	columns.add(linkEdit);

	IColumn<Component, String> linkDelete = new ManteniketLinkColumn<Component, ComponentsDeletePage, String>(Model.of(""),
		ComponentsDeletePage.class, Model.of(""), FontAwesomeIconTypeExt.remove);
	columns.add(linkDelete);
	return columns;
    }

    
    private Collection<? extends IColumn<Component, String>> getColumns() {
	List<IColumn<Component, String>> columns = new ArrayList<IColumn<Component, String>>();
	columns.add(new PropertyColumn<Component, String>(new ResourceModel("head.name"), NAME, NAME));
	columns.add(new PropertyColumn<Component, String>(new ResourceModel("head.type"), TYPE, TYPE));
	columns.add(new AbstractColumn<Component, String>(new ResourceModel("head.km"), KM) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void populateItem(Item<ICellPopulator<Component>> cellItem, String componentId,
		    IModel<Component> rowModel) {
		Component component = rowModel.getObject();
		Double km = getKmComponent(component);
		Label lbl = new Label(componentId, Model.of(km));
		cellItem.add(lbl);
	    }
	});
	// IModel<List<? extends String>> cmb =
	// Model.ofList(Arrays.asList(CyclingType.names()));
	// columns.add( new ChoiceFilteredPropertyColumn<Bici, String, String>(
	// new ResourceModel( "head.type" ), "tipo", "tipo", cmb));
	return columns;
    }

    @Deprecated
    private void listadoOld() {
	log.debug("iniciando listado");
	List<Component> componentes = dao.findAll();
	ListDataProvider<Component> data = new ListDataProvider<Component>(componentes);
	DataView<Component> dataView = new DataView<Component>("rows", data) {

	    @Override
	    protected void populateItem(Item<Component> item) {
		Component component = item.getModelObject();
		Double km = getKmComponent(component);
		RepeatingView rv = new RepeatingView("dataRow");
		rv.add(new Label(rv.newChildId(), new PropertyModel(item.getModel(), "name")));
		rv.add(new Label(rv.newChildId(), new PropertyModel(item.getModel(), "type")));
		rv.add(new Label(rv.newChildId(), Model.of(km)));
		PageParameters params = new PageParameters();
		params.add(ManteniketContracts.ID, component.getId());
		params.add(ManteniketContracts.ENTITY, ManteniketContracts.Entity.COMPONENT);
		params.add(ManteniketContracts.RETURN_PAGE, this.getClass().getName());
		rv.add(new LinkPanel(rv.newChildId(), params, BikeCompListPage.class, getString("btn.uses")));
		rv.add(new LinkPanel(rv.newChildId(), params, ComponentsPage.class, getString("btn.edit")));
		rv.add(new LinkPanel(rv.newChildId(), params, ComponentsDeletePage.class, getString("btn.delete")));
		item.add(rv);
	    }

	};
	dataView.setItemsPerPage(3);
	ManteniketTable table = new ManteniketTable("table");
	table.add(dataView);
	add(table);
	add(new BootstrapPagingNavigator("pagingNavigator", dataView));
    }

    private Double getKmComponent(Component pComp) {
	Double km = 0.0;
	Component component = dao.findOne(pComp.getId());
	Iterator<ComponentUse> it = component.getComponentUses().iterator();
	while (it.hasNext()) {
	    ComponentUse use = it.next();
	    Date fin = use.getFinish() != null ? use.getFinish() : new Date();
	    List<Activity> list = actDao.findByBikeAndDates(use.getBike(), use.getInit(), fin);
	    for (Activity act : list) {
		km += act.getKm();
	    }
	}
	return km;
    }

}

class ComponentSortDataProvider extends SortableDataProvider<Component, String> implements
	IFilterStateLocator<Component> {

    private static final Logger log = LoggerFactory.getLogger(ComponentSortDataProvider.class);
    public IComponentsDAO dao;
    public Component filterState;

    ComponentSortDataProvider(IComponentsDAO dao, Component filtroBase) {
	this.dao = dao;
	filterState = filtroBase;
    }

    @Override
    public Iterator<? extends Component> iterator(long first, long count) {
	int fromPage = 0;
	if (first >= BicisListPage.ITEMS_PAGE) {
	    fromPage = ((int) (first / BicisListPage.ITEMS_PAGE));
	}
	List<Component> list = getList(getSort(), fromPage, BicisListPage.ITEMS_PAGE.intValue());
	return list.iterator();
    }

    private List<Component> getList(SortParam<String> sortParam, int fromPage, int numberOfResults) {
	List<Component> list = null;
	Sort sort = getSort(sortParam);
	list = dao.find(filterState, sort, fromPage, numberOfResults);
	return list;
    }

    private Sort getSort(SortParam<String> sortParam) {
	Sort sort;
	if (sortParam!= null && 
		(sortParam.getProperty().equals(ComponentsListPage.NAME)
		|| sortParam.getProperty().equals(ComponentsListPage.TYPE)
		)
		) {
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
	return sort;
    }

    public long size() {
	return dao.count(filterState);
    }

    @Override
    public IModel<Component> model(Component object) {
	return Model.of(object);
    }

    private Sort defaultSort() {
	return new Sort(Sort.Direction.ASC, ComponentsListPage.NAME);
    }

    @Override
    public Component getFilterState() {
	return filterState;
    }

    @Override
    public void setFilterState(Component state) {
	this.filterState = state;
    }

}