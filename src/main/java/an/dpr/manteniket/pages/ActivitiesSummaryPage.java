package an.dpr.manteniket.pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.bean.ActivitySummaryBean;
import an.dpr.manteniket.bean.ActivityType;
import an.dpr.manteniket.dao.IActivityDao;
import an.dpr.manteniket.exception.ManteniketException;
import an.dpr.manteniket.template.ManteniketPage;
import an.dpr.manteniket.util.DateUtil;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;

public class ActivitiesSummaryPage extends ManteniketPage {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ActivitiesSummaryPage.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
    
    @SpringBean
    private IActivityDao dao;

    // fields
    private DropDownChoice<Integer> cmbYear;
    private DropDownChoice<String> cmbMonth;
    private DropDownChoice<ActivityType> cmbType;
    private Label km;
    private Label time;
    private Label numberActivities;
    private BootstrapButton btnGetData;
    private BootstrapForm form;

    public ActivitiesSummaryPage() {
	this(null);
    }

    public ActivitiesSummaryPage(PageParameters params) {
	initComponents();
	try {
	    defaultData();
	} catch(Exception e){
	    log.error("Error loading data", e);
	}
    }

    private void initComponents() {
	form = new BootstrapForm("form");
	cmbYear = new DropDownChoice<Integer>("cmbYear", getYearsList());
	form.add(cmbYear);
	cmbMonth = new DropDownChoice<String>("cmbMonth", getMonthList());
	cmbMonth.setNullValid(true);
	form.add(cmbMonth);

	ChoiceRenderer<ActivityType> renderAT = new ChoiceRenderer<ActivityType>("name");
	IModel<ActivityType> modelAT = new Model<ActivityType>();
	List<ActivityType> choicesAT = Arrays.asList(ActivityType.values());
	cmbType = new DropDownChoice<ActivityType>("cmbType", modelAT, choicesAT, renderAT);
	cmbType.setNullValid(true);
	form.add(cmbType);

	km = new Label("km");
	form.add(km);
	time = new Label("time");
	form.add(time);
	numberActivities = new Label("numberActivities");
	form.add(numberActivities);
	btnGetData = new BootstrapButton("btnGetData", Type.Default){
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onSubmit(){
		reloadSummary();
	    }
	};
	btnGetData.setLabel(new ResourceModel("btn.getData"));
	form.add(btnGetData);
	add(form);
    }

    private List<? extends String> getMonthList() {
	List<String> list = new ArrayList<String>();
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.DAY_OF_YEAR, 1);
	int january = cal.get(Calendar.MONTH);
	for(int i = january; i<january+12; i++){
	    cal.set(Calendar.MONTH, i);
	    list.add(sdf.format(cal.getTime()));
	}
	Date date = cal.getTime(); 
	System.out.println(sdf.format(date));
	return list;
    }
    
    private List<? extends Integer> getYearsList() {
	 List<Integer> list = new ArrayList<Integer>();
	 int actualYear = Calendar.getInstance().get(Calendar.YEAR);
	 for(int i = actualYear; i > 2000; i--){
	     list.add(i);
	 }
	return list;
    }
    

    private void defaultData() throws ParseException, ManteniketException {
	int year = Calendar.getInstance().get(Calendar.YEAR);
	cmbYear.setModel(Model.of(year));
	String month = sdf.format(new Date());
	cmbMonth.setModel(Model.of(month));
	
	ActivitySummaryBean params = new ActivitySummaryBean();
	Date date = Calendar.getInstance().getTime();
	params.setInitDate(DateUtil.firstDayOfMonth(date));
	params.setFinishDate(DateUtil.lastDayOfMonth(date));
	params.setType(null);
	ActivitySummaryBean summary = dao.getActivitySummary(params);
	km.setDefaultModel(Model.of(summary.getKm()));
	time.setDefaultModel(Model.of(summary.getTime()));
	numberActivities.setDefaultModel(Model.of(summary.getNumberActivities()));
    }

    private void reloadSummary() {
	try{
	    int year = (Integer) cmbYear.getDefaultModelObject();
	    String month = null;
	    if (cmbMonth.getDefaultModelObject() != null){
		month = (String) cmbMonth.getDefaultModelObject();
	    }
	    ActivityType type = (ActivityType)cmbType.getDefaultModelObject();
	    
	    Calendar cal = Calendar.getInstance();
	    Date initDate;
	    Date finishDate;
	    if (month!=null){
		cal.setTime(sdf.parse(month));
		cal.set(Calendar.YEAR, year);
		initDate = DateUtil.firstDayOfMonth(cal.getTime());
		finishDate =DateUtil.lastDayOfMonth(cal.getTime());
	    } else {
		cal.set(Calendar.YEAR, year);
		initDate = DateUtil.firstDayOfYear(cal.getTime());
		finishDate =DateUtil.lastDayOfYear(cal.getTime());
	    }
	    
	    ActivitySummaryBean params = new ActivitySummaryBean();
	    params.setInitDate(initDate );
	    params.setFinishDate(finishDate);
	    params.setType(type);
	    
	    ActivitySummaryBean summary = dao.getActivitySummary(params);
	    km.setDefaultModel(Model.of(summary.getKm()));
	    time.setDefaultModel(Model.of(summary.getTime()));
	    numberActivities.setDefaultModel(Model.of(summary.getNumberActivities()));
	} catch(ParseException e){
	    log.error("Month parse error",e);
	} catch(ManteniketException e){
	    log.error("Error in reloadSummary",e);
	}
    }

}