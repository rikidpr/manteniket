package an.dpr.manteniket.bean;

public enum ComponentType {

    CHAIN(3000), PEDAL(20000), TYRE(3000), SEAT(20000), CABLE(20000), CRANKSET(30000), 
    DERAILLEURS(30000), FRAME(100000);//TODO lo suyo seria entity
    
    private final Integer defaultKmAlert;
    
    private ComponentType(Integer dka){
	defaultKmAlert = dka;
    }
    
    public Integer getDefaultKmAlert(){
	return defaultKmAlert;
    }
}
