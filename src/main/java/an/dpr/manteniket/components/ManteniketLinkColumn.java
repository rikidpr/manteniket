package an.dpr.manteniket.components;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.INamedParameters.NamedPair;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import an.dpr.manteniket.bean.ManteniketBean;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.bean.ManteniketContracts.Entity;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;

/**
 * 
 * @author rsaez
 *
 * @param <T> Bean principal del crud (el que va a ser editado, elminado...)
 * @param <P> Pagina destino
 * @param <S> ordenacion, no tiene mucho sentido aqui
 */
public class ManteniketLinkColumn<T extends ManteniketBean, P extends ManteniketPage, S>  extends AbstractColumn<T,S>{
    
    private static final long serialVersionUID = 1L;
    private Class<P> destination;
    private IModel<String> modelLink;
    private IconType iconType;
    private PageParameters params;

    public ManteniketLinkColumn(IModel<String> header, Class<P> destination, IModel<String> modelLink){
	this(header, destination, modelLink, null, null);
    }

    public ManteniketLinkColumn(IModel<String> header, Class<P> destination, IModel<String> modelLink, PageParameters params){
	this(header, destination, modelLink, null, params);
    }
    
    public ManteniketLinkColumn(IModel<String> header, Class<P> destination, IModel<String> modelLink, IconType iconType){
	this(header, destination, modelLink, iconType, null);
    }
    
    public ManteniketLinkColumn(IModel<String> header, Class<P> destination, IModel<String> modelLink, IconType iconType, PageParameters params){
	super(header);
	this.destination = destination;
	this.modelLink = modelLink;
	this.iconType = iconType;
	this.params= params;
    }
    

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
	PageParameters pageParam = new PageParameters();
	ManteniketBean bean = rowModel.getObject();
	pageParam.add(ManteniketContracts.ID, bean.getId());
	if (this.params != null){
	    for (NamedPair o : this.params.getAllNamed()){
		pageParam.add(o.getKey(), o.getValue());
	    }
	}
	ManteniketLink<P> ml = new ManteniketLink<P>(componentId, destination, pageParam, modelLink);
	if (iconType != null){
	    ml.setIconType(iconType);
	}
	cellItem.add(ml);
	
    }
}