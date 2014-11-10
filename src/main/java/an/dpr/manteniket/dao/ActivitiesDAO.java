package an.dpr.manteniket.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import an.dpr.manteniket.bean.ActivitySummaryBean;
import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.exception.ManteniketException;
import an.dpr.manteniket.repository.ActivitiesRepository;
/**
 * 
 * @author saez
 *
 */
public class ActivitiesDAO implements IActivityDao{

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
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IActivityDao#save(an.dpr.manteniket.domain.Activity)
     */
    @Override
    public Activity save(Activity activity){
	return repo.save(activity);
    }

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IActivityDao#delete(java.lang.Long)
     */
    @Override
    public void delete(Long activityId){
	repo.delete(activityId);
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IActivityDao#findById(java.lang.Long)
     */
    @Override
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
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IActivityDao#find(an.dpr.manteniket.domain.Activity, org.springframework.data.domain.Sort, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<Activity> find(final Activity activity, final Sort sort, final Integer fromPage, final Integer numberOfResults){
	if (activity== null || activity.getUser()==null)
	    throw new IllegalArgumentException("la actividad no puede ser nula ni carecer de usuario");
	return getTransactionTemplate().execute(new TransactionCallback<List<Activity>>() {

	    @Override
	    public List<Activity> doInTransaction(TransactionStatus status) {
		List<Activity> list = null;
		if (fromPage != null){
		    Page<Activity> page= repo.findByUserId(activity.getUser().getId(),new PageRequest(fromPage, numberOfResults, sort));
		    list = page.getContent();
		} else {
		    list = repo.findByUserId(activity.getUser().getId(), sort);
		}
		for(Activity act : list){
		    Hibernate.initialize(act.getBike());
		}
		return list;
	    }
	    
	});
    }

    /**
     * Busca todos pero paginando
     * @param from
     * @param to
     * @return
     */

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

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IActivityDao#findByBikeAndDates(an.dpr.manteniket.domain.Bici, java.util.Date, java.util.Date)
     */
    @Override
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

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IActivityDao#count(an.dpr.manteniket.domain.Activity)
     */
    @Override
    public long count(Activity activity) {
	if (activity== null || activity.getUser()==null)
	    throw new IllegalArgumentException("la actividad no puede ser nula ni carecer de usuario");
	return repo.countByUserId(activity.getUser().getId());
    }

    @Override
    //TODO refactor troceo en metodos especificos (get list, get values)
    public ActivitySummaryBean getActivitySummary(ActivitySummaryBean params)
	    throws ManteniketException {
	ActivitySummaryBean bean = new ActivitySummaryBean();
	List<Activity> lista;
	if (params.getType() == null){
	    lista = repo.findByDateBetween(params.getInitDate(), params.getFinishDate());
	} else {
	    lista = repo.findDatesAndType(params.getInitDate(), params.getFinishDate(), params.getType().name());
	}
	int minutes = 0;
	double km = 0;
	int numberAct = 0;
	for(Activity act:lista){
	    km+=act.getKm();
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(act.getDate());
	    minutes += cal.get(Calendar.HOUR)*60+cal.get(Calendar.MINUTE);
	    numberAct++;
	}
	bean.setKm(km);
	bean.setNumberActivities(numberAct);
	bean.setMinutes(minutes);
	bean.setType(params.getType());
	
	return bean;
	
	
	
    }
    
    
}
