package an.dpr.manteniket.components;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;

public class ConfirmPanel extends Panel{

    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LoggerFactory.getLogger(ConfirmPanel.class);
    
    public ConfirmPanel(String id, String texto, final ConfirmAction actions){
	super(id);
	Alert lblConfirmacion;
	BootstrapButton btnAceptar;
	BootstrapButton btnCancelar;
	BootstrapForm form = new BootstrapForm("confirmForm");
	lblConfirmacion = new Alert("confirmDetails", Model.of(texto), new ResourceModel("lbl.confirm"));
	lblConfirmacion.type(Alert.Type.Warning);
	lblConfirmacion.setCloseButtonVisible(false);
	lblConfirmacion.useInlineHeader(false);
	
	btnAceptar = new BootstrapButton("acceptBtn", Type.Default){
	    private static final long serialVersionUID = 1L;
	    @Override
	    public void onSubmit(){
		log.debug("submit accept button");
		actions.accept();
	    }

	};
	btnAceptar.setLabel(new ResourceModel("btn.accept"));
	btnCancelar = new BootstrapButton("cancelBtn", Type.Warning){
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onSubmit(){
		log.debug("submit cancel button");
		actions.cancel();
	    }
	};
	btnCancelar.setLabel(new ResourceModel("btn.cancel"));
	form.add(lblConfirmacion);
	form.add(btnAceptar);
	form.add(btnCancelar);
	add(form);
    }
    
}
