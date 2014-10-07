package an.dpr.manteniket.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class LinkPanel extends Panel {

    public LinkPanel(String id, final PageParameters params, final Class destino, String texto) {
	super(id);
	Link<String> link = new Link<String>("link") {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick() {
		setResponsePage(destino, params);
	    }

	};
	Label linkLabel = new Label("linkLabel", Model.of(texto));
	link.add(linkLabel);
	add(link);
    }

}