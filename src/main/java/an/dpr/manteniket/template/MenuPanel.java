package an.dpr.manteniket.template;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import an.dpr.manteniket.pages.ActivitiesListPage;
import an.dpr.manteniket.pages.BicisListPage;
import an.dpr.manteniket.pages.ComponentsListPage;

public class MenuPanel extends Panel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MenuPanel(String id) {
	super(id);
	Link activities = new Link("activities") {
	    @Override
	    public void onClick() {
		setResponsePage(ActivitiesListPage.class);
	    }
	};
	activities.add(new Label("lblAct", new ResourceModel("menu.activities")));
	Link bikes = new Link("bikes") {
	    @Override
	    public void onClick() {
		setResponsePage(BicisListPage.class);
	    }
	};
	bikes.add(new Label("lblBikes", new ResourceModel("menu.bikes")));
	Link components = new Link("components") {
	    @Override
	    public void onClick() {
		setResponsePage(ComponentsListPage.class);
	    }
	};
	components.add(new Label("lblComp", new ResourceModel("menu.components")));
	add(bikes);
	add(components);
	add(activities);
    }

}
