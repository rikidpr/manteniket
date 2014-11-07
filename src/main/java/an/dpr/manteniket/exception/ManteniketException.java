package an.dpr.manteniket.exception;

public class ManteniketException extends Exception {
    
    private static final long serialVersionUID = 2679441615597498530L;

    //ERROR CODES
    public static final int CANT_DELETE_DEPENDENCIES = 0;
    
    private int code;
    
    public ManteniketException(String msg) {
	super(msg);
    }
    
    public ManteniketException(String msg, Throwable e) {
	super(msg, e);
    }

    public ManteniketException(int code, String msg, Throwable e) {
	this(msg, e);
	this.code = code; 
    }

    public ManteniketException(int code) {
	super();
	this.code = code; 
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
