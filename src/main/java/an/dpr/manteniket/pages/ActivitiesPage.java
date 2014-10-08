package an.dpr.manteniket.pages;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.extensions.yui.calendar.TimeField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.dao.ActivitiesDAO;
import an.dpr.manteniket.dao.BicisDAO;
import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;

public class ActivitiesPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory
	    .getLogger(ActivitiesPage.class);
    @SpringBean
    private ActivitiesDAO dao;
    @SpringBean
    private BicisDAO bicisDao;

    private TextField<Long> txtId;
    private DateTextField txtDate;
    private TimeField txtTime;
    private TextField<Double> txtKm;
    private TextField<String> txtDesc;
    private DropDownChoice<Bici> cmbBike;
    
    public ActivitiesPage(){
	this(null);
    }

    public ActivitiesPage(PageParameters params) {
	super();
	initComponents();
	if (params != null){
	    loadData(params);
	}
	addValidations();
    }

    private void loadData(PageParameters params) {
	Long id = params.get(ManteniketContracts.ID).toLongObject();
	Activity act = dao.findById(id);
	if (act != null){
	    txtId.setModel(Model.of(act.getActivityId()));
	    txtDate.setModel(Model.of(act.getDate()));
	    txtTime.setModel(Model.of(act.getDate()));
	    txtKm.setModel(Model.of(act.getKm()));
	    txtDesc.setModel(Model.of(act.getDescription()));
	    Model<Bici> choices = new Model<Bici>(act.getBike());
	    cmbBike.setModel(choices);
	}
    }

    private void addValidations() {
	txtDate.setRequired(true);
	txtTime.setRequired(true);
	cmbBike.setRequired(true);
	txtKm.setRequired(true);
	txtKm.add(new RangeValidator<Double>(0.1, 999.9));
	txtDesc.add(new StringValidator(0, 100));
        add(new FeedbackPanel("feedback"));
    }

    private void initComponents() {
	log.debug("iniciando componentes");
	BootstrapForm form = new BootstrapForm("form");
	BootstrapButton saveBtn = new BootstrapButton("btnSave", Type.Default){
	    private static final long serialVersionUID = 1L;
	    public void onSubmit(){
		log.debug("save submit!");
		guardar();
	    }
	    
	};
	saveBtn.setLabel(new ResourceModel("btn.save"));
	form.add(saveBtn);
	BootstrapButton retBtn = new BootstrapButton("btnReturn", Type.Warning){
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
	
	txtId = new TextField<Long>("txtId", Model.of((long)0));
	txtId.setVisible(false);
	txtId.setType(Long.class);
	form.add(txtId);
	
	txtKm = new TextField<Double>("txtKm", Model.of(0.0));
	txtKm.setType(Double.class);
	form.add(new Label("lblKm", new ResourceModel("lbl.km")));
	form.add(txtKm);
	
	txtDesc = new TextField<String>("txtDesc", Model.of(""));
	txtDesc.setType(String.class);
	form.add(new Label("lblDesc", new ResourceModel("lbl.desc")));
	form.add(txtDesc);
	
	txtDate = new DateTextField("txtDate",Model.of(Calendar.getInstance().getTime()));
        DatePicker datePicker = new DatePicker()
        {
            @Override
            protected String getAdditionalJavaScript()
            {
                return "${calendar}.cfg.setProperty(\"navigator\",true,false); ${calendar}.render();";
            }
        };
        datePicker.setShowOnFieldClick(true);
        datePicker.setAutoHide(true);
        txtDate.add(datePicker);
	form.add(new Label("lblDate", new ResourceModel("lbl.date")));
	form.add(txtDate);
	
	txtTime = new TimeField("txtTime", Model.of(Calendar.getInstance().getTime()));
	form.add(new Label("lblTime", new ResourceModel("lbl.time")));
	form.add(txtTime);
	
	List<Bici> bikes = bicisDao.findAll();
	Model<Bici> choices = new Model<Bici>();
	ChoiceRenderer<Bici> render = new ChoiceRenderer<Bici>("codBici", "idBici");
	cmbBike = new DropDownChoice<Bici>("cmbBike", choices, bikes, render);
	form.add(new Label("lblBike", new ResourceModel("lbl.bike")));
	form.add(cmbBike);
	
	add(form);
    }

    private void guardar() {
	Activity act = new Activity();
	if (txtId.getDefaultModelObject()!= null){
	    act.setActivityId((Long)txtId.getDefaultModelObject());
	}
	Date fecha = (Date) txtDate.getDefaultModelObject();
	Calendar cal = Calendar.getInstance();
	cal.setTime(fecha);
	Date hora = (Date) txtTime.getDefaultModelObject();
	Calendar calHour = Calendar.getInstance();
	calHour.setTime(hora);
	
	cal.set(Calendar.HOUR, calHour.get(Calendar.HOUR));
	cal.set(Calendar.MINUTE, calHour.get(Calendar.MINUTE));
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	act.setDate(cal.getTime());
	
	act.setDescription(txtDesc.getDefaultModelObjectAsString());
	act.setKm((Double)txtKm.getDefaultModelObject());
	String idBike = cmbBike.getValue();
	try{
	    Bici bike = new Bici();
	    bike.setIdBici(Long.valueOf(idBike));
	    act.setBike(bike);
	} catch(NumberFormatException e){
	    log.debug(idBike+" no es un valor long valido");
	}
	dao.save(act);
	volver();
    }
    
    private void volver() {
	setResponsePage(ActivitiesListPage.class);
    }
}
