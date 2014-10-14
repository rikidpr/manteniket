package an.dpr.manteniket.components;

import org.apache.wicket.markup.html.WebMarkupContainer;

import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;

/**
 * Tabla de listados para manteniket
 * @author rsaez
 *
 */
public class ManteniketTable extends WebMarkupContainer{

    private static final long serialVersionUID = 1L;

    public ManteniketTable(String id) {
	super(id);
	add(new TableBehavior().striped());
    }

}
