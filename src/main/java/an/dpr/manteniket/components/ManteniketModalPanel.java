package an.dpr.manteniket.components;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;

public class ManteniketModalPanel<T> extends Modal<T> {
    /** serialVersionUID. */
    private static final long serialVersionUID = -5002663900158560343L;
    /** content ID. */
    private static final String CONTENT_ID = "content";

    /**
     * @param varMarkupId modal markup Id
     */
    public ManteniketModalPanel(final String varMarkupId) {
        super(varMarkupId);
        setContent(new EmptyPanel(getContentId()));
    }

    /**
     * @param varMarkupId modal markup Id
     * @param varPanel panel to set in the modal
     */
    public ManteniketModalPanel(final String varMarkupId, final Panel varPanel) {
        super(varMarkupId);
        setContent(varPanel);
    }

    /**
     * @return the contentId
     */
    public static final String getContentId() {
        return CONTENT_ID;
    }

    /**
     * Sets the panel of the modal.
     * 
     * @param component component to set
     * @return this;
     */
    public Modal setContent(final Panel component) {
        if (!component.getId().equals(getContentId())) {
            throw new WicketRuntimeException("Modal content id is wrong. Component ID:" + component.getId()
                    + "; content ID: " + getContentId());
        }

        component.setOutputMarkupId(true);
        addOrReplace(component);
        return this;
    }

    /**
     * Shows the modal window.
     * 
     * @param target
     *            Request target associated with current ajax request.
     * @return 
     */
    public Modal show(final AjaxRequestTarget target) {
        target.add(this);
        appendShowDialogJavaScript(target);
        return this;
    }
    
//    @Override
//    protected String createBasicInitializerScript(final String markupId) {
//        return super.createBasicInitializerScript(markupId) + ".css({witdth:800, height:500});";
//    }
}