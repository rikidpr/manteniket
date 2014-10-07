package an.dpr.manteniket.template;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.ChromeFrameMetaTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.MetaTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.OptimizedMobileViewportMetaTag;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;

public class BootstrapBasePage extends WebPage{

    private static final long serialVersionUID = -5335602068386946371L;

    public BootstrapBasePage() {
        super();
    }

    public BootstrapBasePage(final PageParameters params) {
        super(params);

        add(new HtmlTag("html"));

        add(new OptimizedMobileViewportMetaTag("viewport"));
        add(new MetaTag("description", Model.of("description"), Model.of("Manteniket-The webapp")));
        add(new MetaTag("author", Model.of("author"), Model.of("anDPR Soft")));
        add(new ChromeFrameMetaTag("chrome-frame"));

        //add header panel
        add(new BootstrapHeaderPanel("header"));
        add(new BootstrapFooterPanel("footer"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        Bootstrap.renderHead(response);
        response.render(CssHeaderItem.forReference(FontAwesomeCssReference.instance()));
    }

}
