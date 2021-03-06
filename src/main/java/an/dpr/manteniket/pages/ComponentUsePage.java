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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.bean.ManteniketContracts.Entity;
import an.dpr.manteniket.components.ComponentFactory;
import an.dpr.manteniket.dao.IBikesDAO;
import an.dpr.manteniket.dao.IComponentUsesDAO;
import an.dpr.manteniket.dao.IComponentsDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;

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
    private IComponentsDAO compDao;
    @SpringBean
    private IBikesDAO bikeDao;
    private ComponentUse bean;
    
    private TextField<Long> txtId;
    private DateTextField txtInit;
    private DateTextField txtFin;
    private DropDownChoice<Bici> cmbBike;
    private DropDownChoice<Component> cmbComp;
    private TextArea<String> txtDesc;
    private Long sourceId;
    private Entity entity;
    
    public ComponentUsePage(){
	this(null);
    }
    
    public ComponentUsePage(PageParameters params){
	super();
	Long id = null;
	if (params!= null){
	    id = !params.get(ManteniketContracts.ID).isEmpty() 
		    ? params.get(ManteniketContracts.ID).toLongObject()
			    : null;
		    if (!params.get(ManteniketContracts.SOURCE_ID).isNull()
			    && !params.get(ManteniketContracts.ENTITY).isNull()){
			sourceId = params.get(ManteniketContracts.SOURCE_ID).toLong(0);
			entity = params.get(ManteniketContracts.ENTITY).toEnum(Entity.class);
		    }
	}
	if (id!=null){
	    bean = cuDao.findOne(id);
	}
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
	Object refObj = getEntityRefObject(params);
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
	    if (refObj != null && refObj instanceof Bici) {
		cmbBike.setDefaultModel(new Model<Bici>((Bici)refObj));
	    } else {
		cmbBike.setDefaultModel(new Model<Bici>());
	    }
	    if (refObj != null && refObj instanceof Component) {
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
		PageParameters params = getReturnParams();
		setResponsePage(BikeCompListPage.class, params);
	    }
	};
	saveBtn.setLabel(new ResourceModel("btn.save"));
	form.add(saveBtn);
	BootstrapButton retBtn = new BootstrapButton("retBtn", ManteniketContracts.BTN_RETURN){
	    private static final long serialVersionUID = 1L;
	    public void onSubmit(){
		PageParameters params = getReturnParams();
		setResponsePage(BikeCompListPage.class, params);
	    }
	    public void onError(){
		PageParameters params = getReturnParams();
		setResponsePage(BikeCompListPage.class, params);
	    }
	};
	retBtn.setLabel(new ResourceModel("btn.return"));
	form.add(retBtn);
	
	txtId = new TextField<Long>("txtId");
	txtId.setVisible(false);
	txtId.setType(Long.class);
	form.add(txtId);
	
	
        txtInit = ComponentFactory.datePickerBootstrap("txtInit");
        form.add(txtInit);
        txtFin = ComponentFactory.datePickerBootstrap("txtFin");
        form.add(txtFin);
        
        ChoiceRenderer<Bici> bikeRender = new ChoiceRenderer<Bici>("codBici", "idBici");
        List<Bici> bikes = bikeDao.findAll(getUser());
        cmbBike = new DropDownChoice<Bici>("cmbBike", bikes, bikeRender);
        form.add(cmbBike);

        ChoiceRenderer<Component> compRender = new ChoiceRenderer<Component>("name", "id");
        List<Component> components = compDao.findAllActives(getUser());
        boolean activo = false;
        if (bean != null){
            for(Component c : components){
        	if (c.getId().equals(bean.getComponent().getId())){
        	    activo = true;
        	}
            }
            if (!activo){
        	components.add(bean.getComponent());
            }
        } else {
            activo = true;
        }
        cmbComp = new DropDownChoice<Component>("cmbComp", components, compRender);
        if (!activo){
            cmbComp.setEnabled(false);
        }
        form.add(cmbComp);
        
        txtDesc = new TextArea<String>("txtDesc");
        txtDesc.setType(String.class);
        form.add(txtDesc);
        
        add(form);
    }
    
    private PageParameters getReturnParams() {
	PageParameters params = new PageParameters();
	if (sourceId != null)
	    params.add(ID, sourceId);
	if (entity != null)
	    params.add(ENTITY, entity);
	return params;
    }
    
    private Object getEntityRefObject(PageParameters params) {
	Object object = null;
	if (params != null && !params.get(ManteniketContracts.SOURCE_ID).isNull()
		&& !params.get(ManteniketContracts.ENTITY).isNull()){
	    try{
		long id = params.get(ManteniketContracts.SOURCE_ID).toLong(0);
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
	    } catch(NumberFormatException e){
		log.error("error de formato", e);
	    }
	}
	return object;
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
	use.setUser(getUser());
	
	cuDao.save(use);
	
    }
}
