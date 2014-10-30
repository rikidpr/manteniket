package an.dpr.manteniket.pages;

import static an.dpr.manteniket.bean.ManteniketContracts.ENTITY;
import static an.dpr.manteniket.bean.ManteniketContracts.ID;

import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.bean.ManteniketContracts.Entity;
import an.dpr.manteniket.dao.ComponentesDAO;
import an.dpr.manteniket.dao.IBikesDAO;
import an.dpr.manteniket.dao.IComponentUsesDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig.TodayButton;

/**
 * Page for the add and edit of uses of components.
 * @author rsaez
 *
 */
public class ComponentUsePage extends ManteniketPage{

    private static final Logger log = LoggerFactory.getLogger(ComponentUsePage.class);
    @SpringBean
    private IComponentUsesDAO cuDao;
    @SpringBean
    private ComponentesDAO compDao;
    @SpringBean
    private IBikesDAO bikeDao;
    private ComponentUse bean;
    
    private TextField<Long> txtId;
    private DateTextField txtInit;
    private org.apache.wicket.extensions.markup.html.form.DateTextField txtFin;
    private DropDownChoice<Bici> cmbBike;
    private DropDownChoice<Component> cmbComp;
    private TextArea<String> txtDesc;
    private long sourceId;
    private Entity entity;
    
    public ComponentUsePage(){
	this(null);
    }
    
    public ComponentUsePage(PageParameters params){
	super();
	initComponents(params);
	loadData(params);
	addValidations();
    }

    private void addValidations() {
	/* TODO  pending validations
	 * -finish date after init date
	 * -a component is only in one row/bike at the same time 
	 */
	txtInit.setRequired(true);
	cmbBike.setRequired(true);
	cmbComp.setRequired(true);
	add(new FeedbackPanel("feedback"));
    }

    private void loadData(PageParameters params) {
	Long id = null;
	Object refObj = getEntityRefObject(params);
	if (params!= null){
	    id = !params.get(ManteniketContracts.ID).isEmpty() 
		    ? params.get(ManteniketContracts.ID).toLongObject()
			    : null;
	    entity = params.get(ManteniketContracts.ENTITY)
		    .toEnum(Entity.class);
	    sourceId = params.get(ManteniketContracts.SOURCE_ID).toLong();
	}
	if (id!=null){
	    bean = cuDao.findOne(id);
	}
	if (bean != null){
	    txtId.setDefaultModel(Model.of(bean.getId()));
	    txtInit.setDefaultModel(Model.of(bean.getInit()));
	    txtFin.setDefaultModel(Model.of(bean.getFinish()));
	    cmbBike.setDefaultModel(new Model<Bici>(bean.getBike()));
	    cmbComp.setDefaultModel(new Model<Component>(bean.getComponent()));
	    txtDesc.setDefaultModel(Model.of(bean.getDescrip()));
	    log.debug("Uso:"+bean.toString());
	} else {
	    txtId.setDefaultModel(Model.of(""));
	    txtInit.setDefaultModel(Model.of(new Date()));
	    txtFin.setDefaultModel(Model.of(new Date()));
	    txtDesc.setDefaultModel(Model.of(""));
	    if (refObj instanceof Bici) {
		cmbBike.setDefaultModel(new Model<Bici>((Bici)refObj));
	    } else {
		cmbBike.setDefaultModel(new Model<Bici>());
	    }
	    if (refObj instanceof Component) {
		Component comp = (Component) refObj;
		cmbComp.setDefaultModel(new Model<Component>(comp));
	    } else {
		cmbComp.setDefaultModel(new Model<Component>());
	    }
	}
	
    }

    private void initComponents(final PageParameters params) {
	log.info("inicio");

	
//	final Page returnTo = params.get(ManteniketContracts.RETURN_PAGE).to(Page.class);
	
	BootstrapForm form = new BootstrapForm("form");
	BootstrapButton saveBtn = new BootstrapButton("saveBtn", ManteniketContracts.BTN_SAVE){
	    private static final long serialVersionUID = 1L;

	    public void onSubmit(){
		save();
		PageParameters params = new PageParameters();
		params.add(ID, sourceId);
		params.add(ENTITY, entity);
		setResponsePage(BikeCompListPage.class, params);
	    }
	};
	saveBtn.setLabel(new ResourceModel("btn.save"));
	form.add(saveBtn);
	BootstrapButton retBtn = new BootstrapButton("retBtn", ManteniketContracts.BTN_RETURN){
	    private static final long serialVersionUID = 1L;
	    public void onSubmit(){
		PageParameters params = new PageParameters();
		params.add(ID, sourceId);
		params.add(ENTITY, entity);
		setResponsePage(BikeCompListPage.class, params);
	    }
	    public void onError(){
		PageParameters params = new PageParameters();
		params.add(ID, sourceId);
		params.add(ENTITY, entity);
		setResponsePage(BikeCompListPage.class, params);
	    }
	};
	retBtn.setLabel(new ResourceModel("btn.return"));
	form.add(retBtn);
	
	txtId = new TextField<Long>("txtId");
	txtId.setVisible(false);
	txtId.setType(Long.class);
	form.add(txtId);
	
	
        txtInit = datePickerBootstrap("txtInit");
        form.add(txtInit);
        txtFin = datePickerBootstrap("txtFin");
        form.add(txtFin);
        
        ChoiceRenderer<Bici> bikeRender = new ChoiceRenderer<Bici>("codBici", "idBici");
        List<Bici> bikes = bikeDao.findAll();
        cmbBike = new DropDownChoice<Bici>("cmbBike", bikes, bikeRender);
        form.add(cmbBike);

        ChoiceRenderer<Component> compRender = new ChoiceRenderer<Component>("name", "id");
        List<Component> components = compDao.findAll();
        cmbComp = new DropDownChoice<Component>("cmbComp", components, compRender);
        form.add(cmbComp);
        
        txtDesc = new TextArea<String>("txtDesc");
        txtDesc.setType(String.class);
        form.add(txtDesc);
        
        add(form);
    }
    
    private Object getEntityRefObject(PageParameters params) {
	Object object = null;
	long id = params.get(ManteniketContracts.SOURCE_ID).toLong();
	Entity entity = params.get(ManteniketContracts.ENTITY).toEnum(Entity.class);
	switch(entity){
	case BIKE:
	    Bici bici = new Bici();
	    bici.setIdBici(id);
	    object = bici;
	    break;
	case COMPONENT:
	    Component component = new Component();
	    component.setId(id);
	    object = component;
	    break;
	}
	return object;
    }

    private DateTextField datePickerBootstrap(String id){
	DateTextFieldConfig config = new DateTextFieldConfig()
    		.autoClose(true)
    		.withView(DateTextFieldConfig.View.Decade)
    		.showTodayButton(TodayButton.TRUE)
    		.highlightToday(true)
    		.withStartDate(new DateTime().withYear(2000))
    		.withFormat("dd/MM/yyyy");
	return new DateTextField(id, config);
    }

    private void save(){
	ComponentUse use = new ComponentUse();
	String id = txtId.getDefaultModelObjectAsString();
	if (id != null && !id.isEmpty()){
	    use.setId((Long)txtId.getDefaultModelObject());
	}
	use.setBike((Bici)cmbBike.getDefaultModelObject());
	use.setComponent((Component)cmbComp.getDefaultModelObject());
	use.setInit((Date)txtInit.getDefaultModelObject());
	use.setFinish((Date)txtFin.getDefaultModelObject());
	use.setDescrip(txtDesc.getDefaultModelObjectAsString());
	
	cuDao.save(use);
	
    }
}
