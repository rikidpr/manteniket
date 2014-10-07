package an.dpr.manteniket.template;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import an.dpr.manteniket.WicketApplication;

public class ManteniketPage extends BootstrapBasePage {
    // public static final String CONTENT_ID = "contentComponent";

    private static final long serialVersionUID = 1L;

    public ManteniketPage() {
	this(null);
    }

    public ManteniketPage(final PageParameters params) {
	super(params);
    }
    
    public <T> Object getBean(String id, Class<T> clase) {
	Object ret = ((WicketApplication) getApplication()).context().getBean(id);
	return clase.cast(ret);
    }

}
