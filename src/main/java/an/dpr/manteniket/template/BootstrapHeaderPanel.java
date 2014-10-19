package an.dpr.manteniket.template;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import an.dpr.manteniket.MainPage;
import an.dpr.manteniket.LogoutPage;
import an.dpr.manteniket.pages.ActivitiesListPage;
import an.dpr.manteniket.pages.BicisListPage;
import an.dpr.manteniket.pages.ComponentsListPage;
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
	NavbarButton<ActivitiesListPage> menuActivities;
	NavbarButton<BicisListPage> menuBikes;
	NavbarButton<ComponentsListPage> menuComponents; 

	menuActivities = new NavbarButton<ActivitiesListPage>(ActivitiesListPage.class, new ResourceModel("menu.activities"));
	menuActivities.setIconType(FontAwesomeIconType.user);
	
	menuBikes = new NavbarButton<BicisListPage>(BicisListPage.class, new ResourceModel("menu.bikes"));
	menuBikes.setIconType(FontAwesomeIconType.beer);
	
	menuComponents = new NavbarButton<ComponentsListPage>(ComponentsListPage.class, new ResourceModel("menu.components"));
	menuComponents.setIconType(FontAwesomeIconType.ambulance);
	
	List<INavbarComponent> list = NavbarComponents.transform(Navbar.ComponentPosition.RIGHT, 
		menuActivities, menuBikes, menuComponents);
	navbar.addComponents(list);
    }

    private void addItemsMenuDropDown(Navbar navbar) {
	DropDownButton dropdown = new NavbarDropDownButton(new ResourceModel("menu.dropdown")) {

	    private static final long serialVersionUID = 1L;

	    @SuppressWarnings({ "rawtypes", "unchecked" })
	    @Override
	    protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
		final List<AbstractLink> subMenu = new ArrayList<AbstractLink>();
		subMenu.add(new MenuBookmarkablePageLink(ActivitiesListPage.class, new ResourceModel("menu.activities"))
			.setIconType(FontAwesomeIconType.user));
		subMenu.add(new MenuBookmarkablePageLink(BicisListPage.class, new ResourceModel("menu.bikes"))
			.setIconType(FontAwesomeIconType.user));
		subMenu.add(new MenuBookmarkablePageLink(ComponentsListPage.class, new ResourceModel("menu.components"))
			.setIconType(FontAwesomeIconType.user));
		return subMenu;
	    }

	};
	dropdown.setType(Type.Warning);

	navbar.addComponents(new ImmutableNavbarComponent(dropdown, Navbar.ComponentPosition.RIGHT));
    }
}
