package an.dpr.manteniket.bean;

public enum CyclingType {

    ROAD, MTB, INDOOR, URBAN;
    
    public static String[] names(){
	CyclingType[] cta = CyclingType.values();
	String[] names = new String[cta.length];
	int count = 0;
	for(CyclingType ct : cta){
	    names[count++] = ct.name();
	}
	return names;
    }
}
