package an.dpr.manteniket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

import an.dpr.manteniket.template.ManteniketPage;

public class MainPage extends ManteniketPage {

    private Label lblBienvenida;
    
    public MainPage(){
	super();		
//	getMenuPanel().setVisible(false);
	lblBienvenida = new Label("lblBienvenida", "bienvenido a manteniket");
	add(lblBienvenida);
    }
}
