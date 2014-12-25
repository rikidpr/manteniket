package an.dpr.manteniket.pages;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.bean.MaintenanceType;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.components.ComponentFactory;
import an.dpr.manteniket.dao.IComponentsDAO;
import an.dpr.manteniket.dao.IMaintenanceDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.Maintenance;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;

public class MaintenancesPage extends ManteniketPage {

    private static final long serialVersionUID = 688353696945311155L;
    private static final Logger log = LoggerFactory.getLogger(MaintenancesPage.class);
    @SpringBean
    private IComponentsDAO compDao;
    @SpringBean
    private IMaintenanceDAO mDao;
    // TODO PAGINA ENTERA POR IMPLEMENTAR

    // <select class="form-control" wicket:id="cmbComponent"></select>
    // <select class="form-control" wicket:id="cmbType"></select>
    private DropDownChoice<Component> cmbComp;
    private DropDownChoice<MaintenanceType> cmbType;
    private TextField<String> txtDesc;
    private TextField<String> txtShop;
    private TextField<Double> txtPrice;
    private TextField<Long> txtId;
    private DateTextField txtDate;
    private BootstrapButton saveBtn;
    private BootstrapButton retBtn;
    private BootstrapForm form;

    private Maintenance bean;

    public MaintenancesPage() {
	this(null);
    }

    public MaintenancesPage(PageParameters params) {
	super(params);
	initComponents();
	loadData(params);
	addValidator();
    }

    private void initComponents() {
	form = new BootstrapForm("form");
	saveBtn = new BootstrapButton("saveBtn", Buttons.Type.Default) {
	    private static final long serialVersionUID = 1L;

	    public void onSubmit() {
		log.debug("save submit!");
		guardar();
	    }
	};
	saveBtn.setLabel(new ResourceModel("btn.save"));
	form.add(saveBtn);
	retBtn = new BootstrapButton("retBtn", Buttons.Type.Warning) {
	    private static final long serialVersionUID = 1L;

	    public void onSubmit() {
		log.debug("ret submit!");
		volver();
	    }

	    public void onError() {
		log.debug("ret error!");
		volver();
	    }
	};
	retBtn.setLabel(new ResourceModel("btn.return"));
	form.add(retBtn);
	
	txtId = new TextField<Long>("txtId");
	txtId.setType(Long.class);
	form.add(txtId);

	txtDesc = new TextField<String>("txtDesc");
	txtDesc.setType(String.class);
	form.add(txtDesc);

	txtShop = new TextField<String>("txtShop");
	txtShop.setType(String.class);
	form.add(txtShop);

	txtPrice = new TextField<Double>("txtPrice");
	txtShop.setType(Double.class);
	form.add(txtPrice);

	txtDate = ComponentFactory.datePickerBootstrap("txtDate");
	form.add(txtDate);

	Model<MaintenanceType> mtypeModel = new Model<MaintenanceType>();
	List<? extends MaintenanceType> mtChoices = Arrays.asList(MaintenanceType.values());
	IChoiceRenderer<? super MaintenanceType> mtRender = new ChoiceRenderer<MaintenanceType>("name");
	cmbType = new DropDownChoice<MaintenanceType>("cmbType", mtypeModel, mtChoices, mtRender);

	cmbComp = getCmbComponente();
	form.add(cmbComp);
    }

    private DropDownChoice<Component> getCmbComponente() {
	ChoiceRenderer<Component> compRender = new ChoiceRenderer<Component>("name", "id");
	List<Component> components = compDao.findAllActives(getUser());
	boolean activo = false;
	if (bean != null) {
	    for (Component c : components) {
		if (c.getId().equals(bean.getComponent().getId())) {
		    activo = true;
		}
	    }
	    if (!activo) {
		components.add(bean.getComponent());
	    }
	}
	DropDownChoice<Component> cmb = new DropDownChoice<Component>("cmbComp", components, compRender);
	if (!activo) {
	    cmb.setEnabled(false);
	}
	return cmb;
    }

    private void guardar() {
	refreshBean();
	try{
	    mDao.save(bean);
	} catch(Exception e){
	    log.error("Error saving maintenance", e);
	}
	volver();
    }
    
    private void refreshBean(){
	if (bean == null){
	    bean = new Maintenance();
	    bean.setUser(getUser());
	}
	if (txtId.getDefaultModelObject() != null){
	    bean.setId((Long)txtId.getDefaultModelObject());
	}
	bean.setComponent((Component)cmbComp.getDefaultModelObject());
	Date fecha = (Date) txtDate.getDefaultModelObject();
	bean.setDate(fecha);
	bean.setDescription(txtDesc.getDefaultModelObjectAsString());
	bean.setPrice((Double)txtPrice.getDefaultModelObject());
	bean.setShop(txtShop.getDefaultModelObjectAsString());
	bean.setType(cmbType.getDefaultModelObjectAsString());
    }

    private void volver() {
	setResponsePage(MaintenancesListPage.class);
    }

    private void loadData(PageParameters params) {
	Long id = null;
	if (params != null){
	    id = params.get(ManteniketContracts.ID).toLongObject();
	}
	if(id != null){
	    bean = mDao.findById(id);
	}
	
	if (bean != null) {
	    txtId.setDefaultModel(Model.of(bean.getId()));
	    txtDate.setDefaultModel(Model.of(bean.getDate()));
	    cmbComp.setDefaultModel(new Model<Component>(bean.getComponent()));
	    MaintenanceType mtype = MaintenanceType.valueOf(bean.getType());
	    cmbType.setDefaultModel(new Model<MaintenanceType>(mtype));
	    txtDesc.setDefaultModel(Model.of(bean.getDescription()));
	    txtPrice.setDefaultModel(Model.of(bean.getPrice()));
	    txtShop.setDefaultModel(Model.of(bean.getShop()));
	    log.debug("Uso:" + bean.toString());
	} else {
	    txtId.setDefaultModel(Model.of(""));
	    txtDate.setDefaultModel(Model.of(new Date()));
	    cmbComp.setDefaultModel(new Model<Component>());
	    cmbType.setDefaultModel(new Model<MaintenanceType>());
	    txtDesc.setDefaultModel(Model.of(""));
	    txtPrice.setDefaultModel(Model.of(""));
	    txtShop.setDefaultModel(Model.of(""));
	}

    }

    private void addValidator() {
	txtDate.setRequired(true);
	cmbComp.setRequired(true);
	cmbType.setRequired(true);
        add(new FeedbackPanel("feedback"));
    }

}
