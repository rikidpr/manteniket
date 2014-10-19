package an.dpr.manteniket.bean;

public enum ActivityType {

    ROAD((short)1), CLUB((short)2), INDOOR((short)3), CYCLOTOURIST((short)4), MTB((short)5), URBAN((short)6);
    
    private short code;

    private ActivityType(short code){
	this.code = code;
    }
    
    public short getCode(){
	return code;
    }
    
    public static ActivityType getByCode(short code){
	ActivityType ret=null;
	for(ActivityType at : values()){
	    if (at.code == code){
		ret = at;
		break;
	    }
	}
	return ret;
    }
    
    
}
