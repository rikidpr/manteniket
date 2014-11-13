package an.dpr.manteniket.pages;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import an.dpr.manteniket.bean.ComponentType;
import an.dpr.manteniket.bean.CyclingType;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.dao.ComponentesDAO;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.template.ManteniketPage;

public class ComponentsPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory
	    .getLogger(ComponentsPage.class);
    @SpringBean
    private ComponentesDAO dao;

    private TextField<Long> txtId;
    private TextField<String> txtName;
    private DropDownChoice<ComponentType> cmbType;
    private TextField<String> txtDescription;
    private TextField<Integer> txtKmAlert;

    public ComponentsPage(){
	this(null);
    }
    
    public ComponentsPage(PageParameters params) {
	super();
	initComponents();
	loadData(params);
	validations();
    }
    
    private void validations() {
	txtName.setRequired(true);
	cmbType.setRequired(true);
	txtDescription.setRequired(true);
	txtKmAlert.setRequired(true);
	
	txtName.add(new StringValidator(3,50));
	txtName.add(new StringValidator(3,300));
	txtKmAlert.add(new RangeValidator<Integer>(1, 100000));
        add(new FeedbackPanel("feedback"));
    }

    private void loadData(PageParameters params){
	Component comp = null;
	if (params != null){
	    comp = new Component();
	    comp.setId(params.get(ManteniketContracts.ID).toLongObject());
	}
	if (comp != null && comp.getId() != null){
	    comp = dao.findOne(comp.getId());
	}
	if (comp != null) {
	    log.debug("rellenando datos");
	    txtId.setModel(Model.of(comp.getId()));
	    txtName.setModel(Model.of(comp.getName()));
	    IModel<ComponentType> typeModel = null;
	    try{
		typeModel = new Model<ComponentType>(ComponentType.valueOf(comp.getType()));
	    } catch(IllegalArgumentException e){
		typeModel = new Model<ComponentType>();
	    }
	    cmbType.setDefaultModel(typeModel);
	    txtDescription.setModel(Model.of(comp.getDescription()));
	    txtKmAlert.setModel(Model.of(comp.getKmAlert()));
	} else {
	    txtId.setModel(Model.of((long)0));
	    txtName.setModel(Model.of(""));
	    cmbType.setDefaultModel(new Model<CyclingType>());
	    txtDescription.setModel(Model.of(""));
	    txtKmAlert.setModel(Model.of(0));
	}
    }

    private void initComponents() {
	log.debug("iniciando componentes");
	BootstrapForm form = new BootstrapForm("form");
	BootstrapButton saveBtn = new BootstrapButton("saveBtn", Type.Default){
	    private static final long serialVersionUID = 1L;
	    public void onSubmit(){
		log.debug("save submit!");
		guardar();
	    }
	};
	saveBtn.setLabel(new ResourceModel("btn.save"));
	form.add(saveBtn);
	BootstrapButton retBtn = new BootstrapButton("retBtn", Type.Warning){
	    private static final long serialVersionUID = 1L;
	    public void onSubmit(){
		log.debug("ret submit!");
		volver();
	    }
	    public void onError(){
		log.debug("ret error!");
		volver();
	    }
	};
	retBtn.setLabel(new ResourceModel("btn.return"));
	form.add(retBtn);
	txtId = new TextField<Long>("txtId");
	txtId.setVisible(false);
	txtId.setType(Long.class);
	txtName = new TextField<String>("txtName");
	txtName.setType(String.class);
	txtDescription = new TextField<String>("txtDesc");
	txtDescription.setType(String.class);
	txtKmAlert = new TextField<Integer>("kmAlert");
	txtKmAlert.setType(Integer.class);
	txtKmAlert.setOutputMarkupId(true);
	
	ChoiceRenderer<ComponentType> render = new ChoiceRenderer<ComponentType>("name");
	List<ComponentType> list = Arrays.asList(ComponentType.values());
	cmbType = new DropDownChoice<ComponentType>("cmbType", list, render);
	cmbType.add(new AjaxFormComponentUpdatingBehavior("onchange") {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void onUpdate(AjaxRequestTarget target) {
		ComponentType ct = cmbType.getModelObject();
		txtKmAlert.setModel(Model.of(ct.getDefaultKmAlert()));
		target.add(txtKmAlert);
	    }
	});
	form.add(txtId);
	form.add(txtName);
	form.add(cmbType);
	form.add(txtDescription);
	form.add(txtKmAlert);
	
	add(form);
	
    }

    private void guardar(){
	log.debug("procedemos a guardar");
	Component comp = new Component();
	String id = txtId.getDefaultModelObjectAsString();
	if (id != null && !id.isEmpty()){
	    comp.setId(Long.valueOf(id));
	}
	comp.setName(txtName.getDefaultModelObjectAsString());
	comp.setDescription(txtDescription.getDefaultModelObjectAsString());
	comp.setType(cmbType.getDefaultModelObjectAsString());
	comp.setKmAlert(txtKmAlert.getModelObject());
	comp.setUser(getUser());
	dao.save(comp);
	setResponsePage(ComponentsListPage.class);
    }
    
    private void volver(){
	setResponsePage(ComponentsListPage.class);
    }
}
