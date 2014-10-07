package an.dpr.manteniket.pages;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.bean.CyclingType;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.dao.BicisDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownButton;

public class BicisPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(BicisPage.class);
    @SpringBean
    private BicisDAO dao;

    private TextField<Long> idBici;
    private TextField<String> codBici;
    private DropDownChoice<CyclingType> tipo;
    private TextField<String> descripcion;

    public BicisPage() {
	this(null);
    }
    
    public BicisPage(PageParameters params) {
	super();
	initComponents();
	loadData(params);
	addValidator();
    }

    private void loadData(PageParameters params) {
	Long id = null;
	Bici bike = null;
	if (params != null){
	    id = params.get(ManteniketContracts.ID).toLongObject();
	}
	if(id != null){
	    bike = dao.findByIdBici(id);
	}
	
	if (bike != null){
	    idBici.setModel(Model.of((long)bike.getIdBici()));
	    codBici.setModel(Model.of(bike.getCodBici()));
	    descripcion.setModel(Model.of(bike.getDescripcion()));
	    try{
		tipo.setModel(new Model<CyclingType>(CyclingType.valueOf(bike.getTipo())));
	    } catch (Exception e){
		log.error("tipo no reconocido", e);
		tipo.setModel(new Model<CyclingType>());
	    }
	} else {
	    idBici.setModel(Model.of((long)0));
	    codBici.setModel(Model.of(""));
	    descripcion.setModel(Model.of(""));
	    tipo.setModel(new Model<CyclingType>());
	}
    }

    private void addValidator() {
	codBici.setRequired(true);
	codBici.add(new StringValidator(3,25));
	tipo.setRequired(true);
	descripcion.add(new StringValidator(5, 50));
        add(new FeedbackPanel("feedback"));
    }

    private void initComponents() {
	log.debug("iniciando componentes");
	Form form = new Form("form");
	BootstrapButton saveBtn = new BootstrapButton("saveBtn", Buttons.Type.Default){
	    private static final long serialVersionUID = 1L;
	    public void onSubmit(){
		log.debug("save submit!");
		guardar();
	    }
	};
	saveBtn.setLabel(new ResourceModel("btn.save"));
	form.add(saveBtn);
	BootstrapButton retBtn= new BootstrapButton("retBtn", Buttons.Type.Warning){
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
	
	idBici = new TextField<Long>("txtId");
	idBici.setVisible(false);
	idBici.setType(Long.class);
	
	codBici = new TextField<String>("txtCod");
	codBici.setType(String.class);
	
	ChoiceRenderer<CyclingType> render = new ChoiceRenderer<CyclingType>("name");
	IModel<CyclingType> model = new Model<CyclingType>();
	List<CyclingType> choices = Arrays.asList(CyclingType.values());
	tipo = new DropDownChoice<CyclingType>("cmbType", model, choices, render);
	descripcion = new TextField<String>("txtDesc");
	
	form.add(idBici);
	form.add(codBici);
	form.add(tipo);
	form.add(descripcion);
	
	add(form);
    }
    
    private void volver(){
	setResponsePage(BicisListPage.class);
    }
    
    private void guardar(){
	Bici bike = new Bici();
	if (idBici.getDefaultModelObject() != null){
	    bike.setIdBici((Long)idBici.getDefaultModelObject());
	}
	bike.setCodBici(codBici.getDefaultModelObjectAsString());
	bike.setDescripcion(descripcion.getDefaultModelObjectAsString());
	bike.setTipo(tipo.getDefaultModelObjectAsString());
	try{
	    dao.save(bike);
	} catch(Exception e){
	    log.error("petada guardando", e);
	}
	setResponsePage(BicisListPage.class);
    }

}
