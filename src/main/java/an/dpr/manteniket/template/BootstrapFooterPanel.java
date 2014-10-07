package an.dpr.manteniket.template;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class BootstrapFooterPanel extends Panel {

    private static final long serialVersionUID = -3281941537551076005L;

    public BootstrapFooterPanel(String id) {
	super(id);
	add(new Label("wicket-version", getApplication().getFrameworkSettings().getVersion()));
    }
}
