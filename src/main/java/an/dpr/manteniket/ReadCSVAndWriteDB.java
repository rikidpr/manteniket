package an.dpr.manteniket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    private static final String filePath = "C:\\Users\\saez\\Documents\\riki\\CyclingCarretera - s2014.csv";
//    private static final String filePath = "C:\\Users\\saez\\Documents\\riki\\CyclingCarretera - s2013.tsv";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    
    public static void main(String...args) throws Exception{
//	listBikes();
	loadCSV();
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

    private static void loadCSV() throws IOException, ParseException{
	File f = new File(filePath);	
	BufferedReader bf = new BufferedReader(new FileReader(f));
	String line;
	User user = new User();
	user.setId(1L);
	EntityManager manager = getManagerJPA();
	countActivities(manager);
	manager.getTransaction().begin();
	while((line = bf.readLine())!=null){
	    String[] split = line.split("\\t");
	    Date d = sdf.parse(split[0]);
	    int minutes = Integer.valueOf(split[1])*60+Integer.valueOf(split[2]);
	    double km = Double.valueOf(split[3]);
	    short hr = Short.valueOf(split[4].isEmpty() ? "0" : split[4]);
	    short type = Short.valueOf(split[5]);
	    String desc = split[6];
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
		try{
		    manager.persist(act);
		} catch(PersistenceException e){
		    e.printStackTrace();
		}
		System.out.println("ADD");
	    } else {
		System.out.println("pesas, skip");
	    }
	}
	manager.getTransaction().commit();
	countActivities(manager);
	System.out.println("done...");
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
