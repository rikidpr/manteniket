package an.dpr.manteniket.exception;

public class ManteniketException extends Exception {

    public ManteniketException(String msg) {
	super(msg);
    }
    
    public ManteniketException(String msg, Throwable e) {
	super(msg, e);
    }

    private static final long serialVersionUID = 2679441615597498530L;

}
