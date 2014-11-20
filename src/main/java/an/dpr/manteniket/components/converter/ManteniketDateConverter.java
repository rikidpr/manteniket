package an.dpr.manteniket.components.converter;

import java.util.Locale;

import org.apache.wicket.datetime.DateConverter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * TODO FALTA IMPLEMENTAR
 * @author saez
 *
 */
public class ManteniketDateConverter extends DateConverter {
    
    private static final long serialVersionUID = 1L;
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final ManteniketDateConverter instance;
    
    static{
	instance = new ManteniketDateConverter(false);
    }

    public ManteniketDateConverter(boolean applyTimeZoneDifference) {
	super(applyTimeZoneDifference);
    }

    @Override
    public String getDatePattern(Locale locale) {
	return DATE_FORMAT;
    }

    @Override
    protected DateTimeFormatter getFormat(Locale locale) {
	return DateTimeFormat.forPattern(DATE_FORMAT);
    }

    public static DateConverter getInstance() {
	return instance;
    }

}
