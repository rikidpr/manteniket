package an.dpr.manteniket.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.dao.ActivitiesDAO;
import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.template.ManteniketPage;

public class ConfirmPanel extends Panel{

    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LoggerFactory.getLogger(ConfirmPanel.class);
    @SpringBean
    private ActivitiesDAO dao;
    //componentes
    private Label lblConfirmacion;
    private Button btnAceptar;
    private Button btnCancelar;
    private Form form;
    
    public ConfirmPanel(String id, String texto, final ConfirmAction actions){
	super(id);
	form = new Form("confirmForm");
	lblConfirmacion = new Label("confirmDetails", Model.of(texto));
	
	btnAceptar = new Button("acceptBtn"){
	    private static final long serialVersionUID = 1L;
	    @Override
	    public void onSubmit(){
		log.debug("submit accept button");
		actions.accept();
	    }

	};
	btnCancelar = new Button("cancelBtn"){
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onSubmit(){
		log.debug("submit cancel button");
		actions.cancel();
	    }
	};
	form.add(lblConfirmacion);
	form.add(btnAceptar);
	form.add(btnCancelar);
	add(form);
    }
    
}
