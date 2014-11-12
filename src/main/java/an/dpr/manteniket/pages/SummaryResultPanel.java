package an.dpr.manteniket.pages;

import java.text.DecimalFormat;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import an.dpr.manteniket.bean.ActivitySummaryBean;

public class SummaryResultPanel extends Panel {

    private static final long serialVersionUID = 1L;
    private static final DecimalFormat df = new DecimalFormat("#0.00");

    private Label km;
    private Label time;
    private Label numberActivities;
    private Label compareKm;
    private Label compareTime;
    private Label compareNumAct;
    
    public SummaryResultPanel(String id) {
	super(id);
	panelResultados();
    }

    private void panelResultados() {

	km = new Label("km");
	add(km);
	time = new Label("time");
	add(time);
	numberActivities = new Label("numberActivities");
	add(numberActivities);
	compareKm = new Label("compareKm");
	add(compareKm);
	compareTime = new Label("compareTime");
	add(compareTime);
	compareNumAct = new Label("compareNumAct");
	add(compareNumAct);

    }
    
    public void setResultados(ActivitySummaryBean summary, ActivitySummaryBean compare){
	km.setDefaultModel(Model.of(df.format(summary.getKm())));
	time.setDefaultModel(Model.of(summary.getTime()));
	numberActivities.setDefaultModel(Model.of(summary.getNumberActivities()));
	
	if (compare != null){
	    double difKm = summary.getKm()-compare.getKm();
	    compare.setMinutes(summary.getMinutes()-compare.getMinutes());
	    int difNA = summary.getNumberActivities()-compare.getNumberActivities();
	    
	    compareKm.setDefaultModel(Model.of("("+df.format(difKm)+")"));
	    compareTime.setDefaultModel(Model.of("("+compare.getTime()+")"));
	    compareNumAct.setDefaultModel(Model.of("("+difNA+")"));
	} else {
	    compareKm.setDefaultModel(Model.of(""));
	    compareTime.setDefaultModel(Model.of(""));
	    compareNumAct.setDefaultModel(Model.of(""));
	}
    }

}
