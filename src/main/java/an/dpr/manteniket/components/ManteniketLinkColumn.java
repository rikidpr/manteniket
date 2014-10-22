package an.dpr.manteniket.components;

import org.apache.wicket.Page;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import an.dpr.manteniket.bean.ManteniketBean;
import an.dpr.manteniket.bean.ManteniketContracts;
import an.dpr.manteniket.bean.ManteniketContracts.Entity;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;

public class ManteniketLinkColumn<T extends ManteniketBean, P extends ManteniketPage, S>  extends AbstractColumn<T,S>{
    
    private static final long serialVersionUID = 1L;
    private Class<P> destination;
    private IModel<String> modelLink;
    private IconType iconType;
    private Entity entity;

    public ManteniketLinkColumn(IModel<String> header, Class<P> destination, IModel<String> modelLink){
	this(header, destination, modelLink, null, null);
    }

    public ManteniketLinkColumn(IModel<String> header, Class<P> destination, IModel<String> modelLink, Entity entity){
	this(header, destination, modelLink, null, entity);
    }
    
    public ManteniketLinkColumn(IModel<String> header, Class<P> destination, IModel<String> modelLink, IconType iconType){
	this(header, destination, modelLink, iconType, null);
    }
    
    public ManteniketLinkColumn(IModel<String> header, Class<P> destination, IModel<String> modelLink, IconType iconType, Entity entity){
	super(header);
	this.destination = destination;
	this.modelLink = modelLink;
	this.iconType = iconType;
	this.entity= entity;
    }
    

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
	PageParameters params = new PageParameters();
	ManteniketBean bean = rowModel.getObject();
	params.add(ManteniketContracts.ID, bean.getId());
	if(entity != null){
	    params.add(ManteniketContracts.ENTITY, entity);
	}
	ManteniketLink<P> ml = new ManteniketLink<P>(componentId, destination, params, modelLink);
	if (iconType != null){
	    ml.setIconType(iconType);
	}
	cellItem.add(ml);
	
    }
}