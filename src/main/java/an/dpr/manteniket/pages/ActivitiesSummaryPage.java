package an.dpr.manteniket.pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.bean.ActivitySummaryBean;
import an.dpr.manteniket.bean.ActivityType;
import an.dpr.manteniket.components.ComponentFactory;
import an.dpr.manteniket.components.ManteniketModalPanel;
import an.dpr.manteniket.dao.IActivityDao;
import an.dpr.manteniket.exception.ManteniketException;
import an.dpr.manteniket.template.ManteniketPage;
import an.dpr.manteniket.util.DateUtil;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;

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
    private CheckBox chkCompare;
    private CheckBox chkFilterDates;//TODO bootstrapcheckbox (teniamos problemas)
    private DateTextField initDate;
    private DateTextField finishDate;
    private BootstrapAjaxButton link;
    private BootstrapForm form;
    
    private ManteniketModalPanel<SummaryResultPanel> modalResultados;//wciket bootstrap
    
    
    private boolean compare = false;
    private boolean filterDates = false;
    private SummaryResultPanel pnl;

    public ActivitiesSummaryPage() {
	this(null);
    }

    public ActivitiesSummaryPage(PageParameters params) {
	initComponents();
	int year = Calendar.getInstance().get(Calendar.YEAR);
	cmbYear.setModel(Model.of(year));
	String month = sdf.format(new Date());
	cmbMonth.setModel(Model.of(month));
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

	initDate = ComponentFactory.datePickerBootstrap("initDate");
	initDate.setModel(Model.of(new Date()));
	initDate.setEnabled(filterDates);
	form.add(initDate);
	finishDate = ComponentFactory.datePickerBootstrap("finishDate");
	finishDate.setModel(Model.of(new Date()));
	finishDate.setEnabled(filterDates);
	form.add(finishDate);
	chkFilterDates = new AjaxCheckBox("filterDates",new PropertyModel<Boolean>(this,"filterDates")){
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void onUpdate(AjaxRequestTarget target) {
		initDate.setEnabled(chkFilterDates.getModelObject());
		finishDate.setEnabled(chkFilterDates.getModelObject());
		target.add(initDate);
		target.add(finishDate);
	    }
	};
	chkFilterDates.setDefaultModel(Model.of(Boolean.FALSE));
	form.add(chkFilterDates);
	
	chkCompare = new CheckBox("compare",new PropertyModel<Boolean>(this, "compare"));
	form.add(chkCompare);

	add(form);
	link = new BootstrapAjaxButton("link", Type.Default) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void onSubmit(AjaxRequestTarget target, Form<?> form){
		reloadSummary(target);
	    }
	};
	link.setLabel(new ResourceModel("btn.getData"));
	form.add(link);
	panelResultados();
    }
    
    private void panelResultados(){
	modalResultados = new ManteniketModalPanel<SummaryResultPanel>("modalResultados");
	modalResultados.header(new ResourceModel("summary.tittle"));
	pnl = new SummaryResultPanel(ManteniketModalPanel.getContentId());
	modalResultados.setContent(pnl);
	add(modalResultados);
    }
    
    private List<? extends String> getMonthList() {
	List<String> list = new ArrayList<String>();
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.DAY_OF_YEAR, 1);
	int january = cal.get(Calendar.MONTH);
	for (int i = january; i < january + 12; i++) {
	    cal.set(Calendar.MONTH, i);
	    list.add(sdf.format(cal.getTime()));
	}
	return list;
    }

    private List<? extends Integer> getYearsList() {
	List<Integer> list = new ArrayList<Integer>();
	int actualYear = Calendar.getInstance().get(Calendar.YEAR);
	for (int i = actualYear; i > 2000; i--) {
	    list.add(i);
	}
	return list;
    }

    private void reloadSummary(AjaxRequestTarget target) {
	try {
	    ActivitySummaryBean compare = null;
	    ActivitySummaryBean summary = null;
	    ActivityType type = (ActivityType) cmbType.getDefaultModelObject();

	    ActivitySummaryBean params = new ActivitySummaryBean();
	    params.setType(type);
	    params = getSelectedDates(params);

	    summary = dao.getActivitySummary(params);
	    
	    if (isCompare()){//FIXME PUSH TO NEW METHOD 
		
		Calendar cal= Calendar.getInstance();
		//init -1year
		cal.setTime(params.getInitDate());
		cal.add(Calendar.YEAR, -1);
		params.setInitDate(cal.getTime());
		//finish -1year
		cal.setTime(params.getFinishDate());
		cal.add(Calendar.YEAR, -1);
		params.setFinishDate(cal.getTime());
		
		compare = dao.getActivitySummary(params);
	    }
	    
	    pnl.setResultados(summary, compare);
	    modalResultados.show(target);
	} catch (ParseException e) {
	    log.error("Month parse error", e);
	} catch (ManteniketException e) {
	    log.error("Error in reloadSummary", e);
	}
    }

    private ActivitySummaryBean getSelectedDates(ActivitySummaryBean bean) throws ParseException {
	Date initDate;
	Date finishDate;
	//si se rellenan las fechas especificas, prou
	initDate = this.initDate.getModelObject();
	finishDate = this.finishDate.getModelObject();
	//en caso de que no se rellenen, se tira de anyo/mes
	if (!Boolean.TRUE.equals(chkFilterDates.getDefaultModelObject()) || initDate == null || finishDate == null){
	    int year = (Integer) cmbYear.getDefaultModelObject();
	    String month = null;
	    if (cmbMonth.getDefaultModelObject() != null) {
		month = (String) cmbMonth.getDefaultModelObject();
	    }
	    
	    Calendar cal = Calendar.getInstance();
	    if (month != null) {
		cal.setTime(sdf.parse(month));
		cal.set(Calendar.YEAR, year);
		initDate = DateUtil.firstDayOfMonth(cal.getTime());
		finishDate = DateUtil.lastDayOfMonth(cal.getTime());
	    } else {
		cal.set(Calendar.YEAR, year);
		initDate = DateUtil.firstDayOfYear(cal.getTime());
		finishDate = DateUtil.lastDayOfYear(cal.getTime());
	    }
	}

	bean.setInitDate(initDate);
	bean.setFinishDate(finishDate);
	
	return bean;
    }

    public boolean isCompare() {
        return compare;
    }

    public void setCompare(boolean compare) {
        this.compare = compare;
    }

    public boolean isFilterDates() {
        return filterDates;
    }

    public void setFilterDates(boolean filterDates) {
        this.filterDates = filterDates;
    }

}