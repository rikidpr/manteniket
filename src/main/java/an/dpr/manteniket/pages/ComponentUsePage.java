package an.dpr.manteniket.pages;

import java.util.Date;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.repository.BicisRepository;
import an.dpr.manteniket.repository.ComponentUsesRepository;
import an.dpr.manteniket.repository.ComponentesRepository;
import an.dpr.manteniket.template.ManteniketPage;

/**
 * Page for the add and edit of uses of components.
 * @author rsaez
 *
 */
public class ComponentUsePage extends ManteniketPage{

    private static final Logger log = LoggerFactory.getLogger(ComponentUsePage.class);
    @SpringBean
    private ComponentUsesRepository repo;
    @SpringBean
    private ComponentesRepository repoComp;
    @SpringBean
    private BicisRepository repoBike;
    
    private Long bikeId;
    private TextField<Long> txtId;
    private DateTextField txtInit;
    private DateTextField txtFin;
    private DropDownChoice<Bici> cmbBike;
    private DropDownChoice<Component> cmbComp;
    private TextArea<String> txtDesc;
    
    public ComponentUsePage(){
	this(null);
    }
    
    public ComponentUsePage(PageParameters params){
	super();
	initComponents();
	Long id = null;
	if (params!= null){
	    bikeId = params.get("bikeId").toLongObject();
	    id = !params.get(ManteniketContracts.ID).isEmpty() 
		    ? params.get(ManteniketContracts.ID).toLongObject()
		    : null;
	}
	loadData(id);
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

    private void loadData(Long id) {
	ComponentUse use = null;
	if (id!=null){
	    use = repo.findOne(id);
	}
	if (use != null){
	    txtId.setDefaultModel(Model.of(use.getId()));
	    txtInit.setDefaultModel(Model.of(use.getInit()));
	    txtFin.setDefaultModel(Model.of(use.getFinish()));
	    cmbBike.setDefaultModel(new Model<Bici>(use.getBike()));
	    cmbComp.setDefaultModel(new Model<Component>(use.getComponent()));
	    txtDesc.setDefaultModel(Model.of(use.getDescrip()));
	} else {
	    txtId.setDefaultModel(Model.of(""));
	    txtInit.setDefaultModel(Model.of(new Date()));
	    txtFin.setDefaultModel(Model.of(new Date()));
	    cmbBike.setDefaultModel(new Model<Bici>());
	    cmbComp.setDefaultModel(new Model<Component>());
	    txtDesc.setDefaultModel(Model.of(""));
	}
    }

    private void initComponents() {
	log.info("inicio");
	Form form = new Form("form");
	Button saveBtn = new Button("saveBtn"){
	    public void onSubmit(){
		save();
	    }
	};
	form.add(saveBtn);
	Button retBtn = new Button("retBtn"){
	    public void onSubmit(){
		returnPage();
	    }
	    public void onError(){
		returnPage();
	    }
	};
	form.add(retBtn);
	
	txtId = new TextField<Long>("txtId");
	txtId.setVisible(false);
	txtId.setType(Long.class);
	form.add(txtId);
	
	txtInit=new DateTextField("txtInit");
	DatePicker datePicker = new DatePicker(){

	    @Override
            protected String getAdditionalJavaScript()
            {
                return "${calendar}.cfg.setProperty(\"navigator\",true,false); ${calendar}.render();";
            }
        };
        datePicker.setShowOnFieldClick(true);
        datePicker.setAutoHide(true);
        txtInit.add(datePicker);
        form.add(txtInit);
        
        txtFin = new DateTextField("txtFin");
        datePicker = new DatePicker(){
            
            @Override
            protected String getAdditionalJavaScript()
            {
        	return "${calendar}.cfg.setProperty(\"navigator\",true,false); ${calendar}.render();";
            }
        };
        datePicker.setShowOnFieldClick(true);
        datePicker.setAutoHide(true);
        txtFin.add(datePicker);
        form.add(txtFin);
        
        ChoiceRenderer<Bici> bikeRender = new ChoiceRenderer<Bici>("codBici", "idBici");
        List<Bici> bikes = repoBike.findAll();
        cmbBike = new DropDownChoice<Bici>("cmbBike", bikes, bikeRender);
        form.add(cmbBike);

        ChoiceRenderer<Component> compRender = new ChoiceRenderer<Component>("name", "id");
        List<Component> components = repoComp.findAll();
        cmbComp = new DropDownChoice<Component>("cmbComp", components, compRender);
        form.add(cmbComp);
        
        txtDesc = new TextArea<String>("txtDesc");
        txtDesc.setType(String.class);
        form.add(txtDesc);
        
        add(form);
    }
    
    private void returnPage(){
	PageParameters params = new PageParameters();
	params.set(ManteniketContracts.ID, bikeId);
	setResponsePage(BikeCompListPage.class, params);//TODO config the components page source
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
	
	repo.save(use);
	
	returnPage();
    }
}
