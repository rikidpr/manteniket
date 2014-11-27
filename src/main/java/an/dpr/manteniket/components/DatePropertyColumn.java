package an.dpr.manteniket.components;

import java.util.Date;

import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public class DatePropertyColumn<T, S> extends PropertyColumn<T, S> {
    private static final long serialVersionUID = 1L;
    private DateConverter converter;

    public DatePropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression,
	    DateConverter converter) {
	super(displayModel, sortProperty);
	this.converter = converter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
	item.add(new DateLabel(componentId, (IModel<Date>) createLabelModel(rowModel), converter));
    }

}