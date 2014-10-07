package an.dpr.manteniket.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.repository.ActivitiesRepository;

public class ActivitiesDAO {

    private static final Logger log = LoggerFactory.getLogger(ActivitiesDAO.class);
    @Autowired
    private ActivitiesRepository repo;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;
    
    public ActivitiesDAO(){
	
    }
    
    private TransactionTemplate getTransactionTemplate(){
	if (transactionTemplate == null){
	    transactionTemplate = new TransactionTemplate(transactionManager);
	}
	return transactionTemplate;
    }
    
    public Activity save(Activity activity){
	return repo.save(activity);
    }

    public void delete(Long activityId){
	repo.delete(activityId);
    }
    
    public Activity findById(final Long activityId){
	return getTransactionTemplate().execute(new TransactionCallback<Activity>() {
	    
	    @Override
	    public Activity doInTransaction(TransactionStatus status) {
		Activity act = repo.findOne(activityId);
		Hibernate.initialize(act.getBike());
		return act;
	    }
	    
	});
	
    }
    
    public List<Activity> findAll(){
	return getTransactionTemplate().execute(new TransactionCallback<List<Activity>>() {

	    @Override
	    public List<Activity> doInTransaction(TransactionStatus status) {
		List<Activity> list = repo.findAll();
		for(Activity act : list){
		    Hibernate.initialize(act.getBike());
		}
		return list;
	    }
	    
	});
    }

    public List<Activity> findByDates(final Date ini, final Date fin){

	return getTransactionTemplate().execute(new TransactionCallback<List<Activity>>() {

	    @Override
	    public List<Activity> doInTransaction(TransactionStatus status) {
		
		List<Activity> list = repo.findByDateBetween(ini, fin);
		for(Activity act : list){
		    Hibernate.initialize(act.getBike());
		}
		return list;
	    }
	    
	});
    }

    public List<Activity> findByBikeAndDates(final Bici bike, final Date ini, final Date fin){
	
	return getTransactionTemplate().execute(new TransactionCallback<List<Activity>>() {
	    
	    @Override
	    public List<Activity> doInTransaction(TransactionStatus status) {
		
		List<Activity> list = repo.findByBikeAndDateBetween(bike, ini, fin);
		for(Activity act : list){
		    Hibernate.initialize(act.getBike());
		}
		return list;
	    }
	    
	});
    }
    
    
}
