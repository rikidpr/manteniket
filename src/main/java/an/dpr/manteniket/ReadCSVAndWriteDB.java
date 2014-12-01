package an.dpr.manteniket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.User;

public class ReadCSVAndWriteDB {

    private static final String filePath14 = "C:\\Users\\saez\\Documents\\riki\\CyclingCarretera - s2014.csv";
    private static final String filePath13 = "C:\\Users\\saez\\Documents\\riki\\CyclingCarretera - s2013.tsv";
    private static final String filePath12 = "C:\\Users\\saez\\Documents\\riki\\CyclingCarretera - 2012.tsv";
    private static final String filePath11 = "C:\\Users\\saez\\Documents\\riki\\CyclingCarretera - 2011.tsv";
    private static final String filePath10 = "C:\\Users\\saez\\Documents\\riki\\CyclingCarretera - 2010.tsv";
    private static final String filePath09 = "C:\\Users\\saez\\Documents\\riki\\CyclingCarretera - 2009.tsv";
    private static final SimpleDateFormat sdf2012 = new SimpleDateFormat("MM/dd/yyyy");//2012 ->
    private static final SimpleDateFormat sdf2009 = new SimpleDateFormat("dd/MM/yy");//2009
    
    
    
    public static void main(String...args) throws Exception{
	int year = 2013;
	loadCSV(year);
//	countActivities(getManagerJPA());
    }
    
    private static void listBikes() {
	EntityManager manager = getManagerJPA();
	Query q = manager.createQuery("select b from Bici b", Bici.class);
	List<Bici> list = q.getResultList();
	System.out.println(list.size());
	for(Bici obj:list){
	    System.out.println(obj);
	}
	q = manager.createQuery("select b from Activity b", Activity.class);
	List<Activity> act= q.getResultList();
	System.out.println(act.size());
	for(Activity obj:act){
	    System.out.println(obj);
	}
    }

    private static void loadCSV(int year) throws IOException, ParseException{
	String filePath = getPath(year);
	File f = new File(filePath);	
	BufferedReader bf = new BufferedReader(new FileReader(f));
	String line;
	User user = new User();
	user.setId(1L);
	EntityManager manager = getManagerJPA();
	manager.getTransaction().begin();
	int numLine=1;
	while((line = bf.readLine())!=null){
	    String[] split = line.split("\\t");
	    int index =0;
	    Date d = getSDF(year).parse(split[index++]);
	    int minutes = 0;
	    String stime = split[index];
	    if (stime.contains("'") || stime.contains("h")){
		String[] tiempo = split[index++].split("h");
		int hind=0;
		int min = 0;
		int hour = 0;
		if (tiempo.length>1 || stime.contains("h")){
		    try{
			hour = Integer.valueOf(tiempo[hind++].trim());
		    } catch(Exception e){}
		}
		if (tiempo.length>1 || stime.contains("'")){
		    try{
			String smin = tiempo[hind++].trim();
			if (smin.contains("'")){
			    smin = smin.substring(0, smin.length()-1);
			} 
			min = Integer.valueOf(smin);
		    } catch(Exception e){}
		}
		minutes = hour*60+min;
	    } else {
		int min = 0;
		int hour = 0;
		try{
		    hour = Integer.valueOf(split[index++]);
		} catch(Exception e){}
		try{
		    min = Integer.valueOf(split[index++]);
		} catch(Exception e){}
		minutes = hour*60+min;
	    }
	    double km = 0;
	    try{
		km = Double.valueOf(split[index++]);
	    } catch(Exception e){}
	    short hr = 0;
	    try{
		hr = Short.valueOf(split[index++]);
	    } catch(Exception e){}
	    
	    short type;
	    if (year == 2009){
		index++;//saltamos la velocidad media
		type = Short.valueOf(split[index++]);
		if (type == 2) 
		    type = 4;
		if (type == 0)
		    type = 1;
		if (type==1)
		    type=2;
	    } else {
		type = Short.valueOf(split[index++]);
	    }
//	    
	    //now, for all formats again
	    String desc = "";
	    try{
		desc = split[index++].substring(0,99);
	    } catch(Exception e){}
	    if (type < 6){
		Activity act = new Activity();
		act.setDate(d);
		act.setMinutes(minutes);
		act.setDescription(desc);
		act.setDisabledDate(null);
		act.setHeartRate(hr);
		act.setKm(km);
		act.setType(type);
		act.setUser(user);
		act.setBike(getBike(type, manager));
		System.out.println(act);
		try{
		    manager.persist(act);
		} catch(PersistenceException e){
		    e.printStackTrace();
		}
		System.out.println("ADD"+numLine++);
	    } else {
		System.out.println("pesas, skip"+numLine++);
	    }
	}
	manager.getTransaction().commit();
	countActivities(manager);
	System.out.println("done...");
    }
    
    private static DateFormat getSDF(int year) {
	switch (year){
	case 2009: 
	case 2010:
	case 2011:
	    return sdf2009;
	default:
	    return sdf2012;
	}
    }

    private static String getPath(int year){
	String filePath = null;
	switch (year){
	    case 2009:
		filePath = filePath09;
		break;
	    case 2010:
		filePath = filePath10;
		break;
	    case 2011:
		filePath = filePath11;
		break;
	    case 2012:
		filePath = filePath12;
		break;
	    case 2013:
		filePath = filePath13;
		break;
	    case 2014:
		filePath = filePath14;
		break;
	}
	return filePath;
    }
    
    private static void countActivities(EntityManager manager) {
	Query q = manager.createQuery("select count(a) from Activity a");
	System.out.println("activities in db:"+q.getSingleResult());
    }

    private static Bici getBike(short type, EntityManager manager) {
	Bici bike = new Bici();
	String codBici;
	switch(type){
	case 1:
	case 2:
	case 4:
	    codBici = "ConorWRC";
	    break;
	case 5:
	    codBici = "Trek3700";
	    break;
	case 3:
	    codBici = "Peugeot Mitica";
	    break;
	default: codBici = "";
	}
	Query q = manager.createQuery("select b from Bici b where b.codBici='"+codBici+"'", Bici.class);
	Bici bici = (Bici) q.getSingleResult();
	return bici;
    }

    private static EntityManager getManagerJPA(){
	EntityManagerFactory factory = Persistence.createEntityManagerFactory("manteniketdb");
	EntityManager manager = factory.createEntityManager();
	return manager;
    }
    
}
