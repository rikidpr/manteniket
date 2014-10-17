package an.dpr.manteniket.bean;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;


public class ManteniketContracts {

    public static final String ID = "ID";
    public static final String ENTITY = "MODEL";
    public static final String RETURN_PAGE = "RETURN_PAGE";
    public static final String SOURCE_ID = "SOURCE_ID";
    
    public static final Type BTN_RETURN = Type.Success;
    public static final Type BTN_ADD = Type.Info;
    public static final Type BTN_SAVE = Type.Warning;
    
    public enum Entity {
	COMPONENT, BIKE, ACTIVITY;
    }
    
}
