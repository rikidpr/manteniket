package an.dpr.manteniket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.apache.wicket.resource.JQueryResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.ThemeProvider;
import de.agilecoders.wicket.less.BootstrapLess;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see an.dpr.manteniket.Start#main(String[])
 */
public class WicketApplication extends WebApplication {
    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends WebPage> getHomePage() {
	return LoginPage.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
	getComponentInstantiationListeners().add(
		new SpringComponentInjector(this, context(), true));
	super.init();
	configureBootstrap();
	addResourceReplacement(
		JQueryResourceReference.get(),
		new UrlResourceReference(Url
			.parse("http://code.jquery.com/jquery-1.11.0.min.js")));

	// add your configuration here
    }

    public ApplicationContext context() {
	return WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext());
    }

    private void configureBootstrap() {

	final IBootstrapSettings settings = new BootstrapSettings();
	settings.useCdnResources(true);

	final ThemeProvider themeProvider = new BootswatchThemeProvider(
		BootswatchTheme.Darkly);
	settings.setThemeProvider(themeProvider);

	Bootstrap.install(this, settings);
	BootstrapLess.install(this);
    }
}
