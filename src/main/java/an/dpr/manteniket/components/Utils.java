package an.dpr.manteniket.components;

import java.util.Calendar;

import org.apache.wicket.extensions.yui.calendar.TimeField;
import org.apache.wicket.model.Model;
import org.joda.time.DateTime;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig.TodayButton;

public class Utils {

    public static DateTextField datePickerBootstrap(String id){
  	DateTextFieldConfig config = new DateTextFieldConfig()
      		.autoClose(true)
      		.withView(DateTextFieldConfig.View.Decade)
      		.showTodayButton(TodayButton.TRUE)
      		.highlightToday(true)
      		.withStartDate(new DateTime().withYear(2000))
      		.withFormat("dd/MM/yyyy");
  	return new DateTextField(id, config);
      }

    public static TimeField timePickerBootstrap(String id){
	TimeField tf = new TimeField(id, Model.of(Calendar.getInstance().getTime()));
	return tf;
    }
}
