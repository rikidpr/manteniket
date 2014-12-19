package an.dpr.manteniket.pages;

import java.math.BigDecimal;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;

import an.dpr.manteniket.bean.MaintenanceType;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;

public class MaintenancesPage extends ManteniketPage {

    private static final long serialVersionUID = 688353696945311155L;
    
    //TODO PAGINA ENTERA POR IMPLEMENTAR

//	<select class="form-control" wicket:id="cmbComponent"></select>
//	<select class="form-control" wicket:id="cmbType"></select>
    private DropDownChoice<Component> cmbComp;
    private DropDownChoice<MaintenanceType> cmbType;
    private TextField<String> txtDesc;
    private TextField<String> txtShop;
    private TextField<BigDecimal> txtPrice;
    private DateTextField txtDate;
    private BootstrapButton saveBtn;
    private BootstrapButton retBtn;
    
}
