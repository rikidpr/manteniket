package an.dpr.manteniket.template;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import an.dpr.manteniket.MainPage;
import an.dpr.manteniket.LogoutPage;
import an.dpr.manteniket.components.FontAwesomeIconTypeExt;
import an.dpr.manteniket.pages.ActivitiesListPage;
import an.dpr.manteniket.pages.ActivitiesSummaryPage;
import an.dpr.manteniket.pages.BicisListPage;
import an.dpr.manteniket.pages.BikeCompListPage;
import an.dpr.manteniket.pages.ComponentsListPage;
import an.dpr.manteniket.pages.MaintenancesListPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.INavbarComponent;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.ImmutableNavbarComponent;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;

public class BootstrapHeaderPanel extends Panel {
    
    private static final long serialVersionUID = 1L;

    public BootstrapHeaderPanel(String id) {
	super(id);

	add(navbar());
    }

    //TODO el boton home deberia ser mejor un boton logout, y el titulo dirigir a mainpage
    private Navbar navbar() {
	Navbar navbar = new Navbar("navbar");
	navbar.setInverted(true);
	navbar.setPosition(Navbar.Position.TOP);
	navbar.setBrandName(new ResourceModel("header.title"));

	addItemsMenu(navbar);

	navbar.addComponents(NavbarComponents.transform(
		Navbar.ComponentPosition.RIGHT, 
		new NavbarButton<LogoutPage>(LogoutPage.class, new ResourceModel("header.logout")).setIconType(GlyphIconType.logout)));

	return navbar;
    }

    private void addItemsMenu(Navbar navbar) {
	NavbarButton<ActivitiesSummaryPage> menuSummary;
	NavbarButton<ActivitiesListPage> menuActivities;
	NavbarButton<BicisListPage> menuBikes;
	NavbarButton<ComponentsListPage> menuComponents; 
	NavbarButton<BikeCompListPage> menuUses; 
	NavbarButton<MaintenancesListPage> menuMaintenances; 

	menuSummary = new NavbarButton<ActivitiesSummaryPage>(ActivitiesSummaryPage.class, new ResourceModel("menu.summary"));
	menuSummary.setIconType(FontAwesomeIconTypeExt.area_chart);
	
	menuActivities = new NavbarButton<ActivitiesListPage>(ActivitiesListPage.class, new ResourceModel("menu.activities"));
	menuActivities.setIconType(FontAwesomeIconTypeExt.area_chart);
	
	menuBikes = new NavbarButton<BicisListPage>(BicisListPage.class, new ResourceModel("menu.bikes"));
	menuBikes.setIconType(FontAwesomeIconTypeExt.bicycle);
	
	menuComponents = new NavbarButton<ComponentsListPage>(ComponentsListPage.class, new ResourceModel("menu.components"));
	menuComponents.setIconType(FontAwesomeIconType.cog);

	menuUses= new NavbarButton<BikeCompListPage>(BikeCompListPage.class, new ResourceModel("menu.uses"));
	menuUses.setIconType(FontAwesomeIconType.plus_circle);
	
	menuMaintenances= new NavbarButton<MaintenancesListPage>(MaintenancesListPage.class, new ResourceModel("menu.maintenances"));
	menuMaintenances.setIconType(FontAwesomeIconType.wrench);
	
	List<INavbarComponent> list = NavbarComponents.transform(Navbar.ComponentPosition.RIGHT, 
		menuActivities, menuBikes, menuComponents, menuUses, menuSummary, menuMaintenances);
	navbar.addComponents(list);
    }

}
