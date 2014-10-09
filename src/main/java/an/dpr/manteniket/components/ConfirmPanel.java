package an.dpr.manteniket.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.dao.ActivitiesDAO;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;

public class ConfirmPanel extends Panel{

    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LoggerFactory.getLogger(ConfirmPanel.class);
    @SpringBean
    private ActivitiesDAO dao;
    //componentes
    private Alert lblConfirmacion;
    private BootstrapButton btnAceptar;
    private BootstrapButton btnCancelar;
    private BootstrapForm form;
    
    public ConfirmPanel(String id, String texto, final ConfirmAction actions){
	super(id);
	form = new BootstrapForm("confirmForm");
//	lblConfirmacion = new Label("confirmDetails", Model.of(texto));
	lblConfirmacion = new Alert("confirmDetails", Model.of(texto), new ResourceModel("lbl.confirm"));
	lblConfirmacion.type(Alert.Type.Danger);
	
	btnAceptar = new BootstrapButton("acceptBtn", Type.Warning){
	    private static final long serialVersionUID = 1L;
	    @Override
	    public void onSubmit(){
		log.debug("submit accept button");
		actions.accept();
	    }

	};
	btnAceptar.setLabel(new ResourceModel("btn.accept"));
	btnCancelar = new BootstrapButton("cancelBtn", Type.Default){
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
