package an.dpr.manteniket.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.domain.User;
import an.dpr.manteniket.exception.ManteniketException;
import an.dpr.manteniket.repository.ComponentesRepository;

/**
 * Dao del entity componentes
 * 
 * @author rsaez
 */
public class ComponentesDAO extends ManteniketDAO implements IComponentsDAO{

    private static final Logger log = LoggerFactory
	    .getLogger(ComponentesDAO.class);
    @Autowired
    private ComponentesRepository repo;
    @Autowired
    private IActivityDao activityDao;
    @Autowired
    private IComponentUsesDAO componentUsesDAO;
    
    public Component save(Component comp){
	return repo.save(comp);
	// TODO manejo excepciones sql
    }
    
    public void delete(Long id) throws ManteniketException{
	try{
	    repo.delete(id);
	} catch(org.springframework.dao.DataIntegrityViolationException e){
	    log.error("The component can't deleted", e);
	    throw new ManteniketException(ManteniketException.CANT_DELETE_DEPENDENCIES);
	}
    }

    public Component findOne(final Long id) {
	log.debug("params: id " + id);
	return getTransactionTemplate().execute(new TransactionCallback<Component>() {
	    
	    @Override
	    public Component doInTransaction(TransactionStatus status) {
		Component comp = repo.findOne(id);
		Hibernate.initialize(comp.getComponentUses());
		Iterator<ComponentUse> it = comp.getComponentUses().iterator();
		while(it.hasNext()){
		    ComponentUse use = it.next();
		    Hibernate.initialize(use.getBike());
		}
		return comp;
	    }
	    
	});
	// TODO manejo excepciones sql
    }

    @Override
    public List<Component> findAll() {
	return repo.findAll();
    }

    @Override
    public List<Component> findAllActives(final User user) {
	return getTransactionTemplate().execute(new TransactionCallback<List<Component>>() {
	    
	    @Override
	    public List<Component> doInTransaction(TransactionStatus status) {
		List<Component> list = repo.findAllActives(user.getId());
		for (Component comp : list){
		    Hibernate.initialize(comp.getComponentUses());
		}
		return list;
	    }
	    
	});
    }
    
    public ComponentesRepository getRepo() {
	return repo;
    }

    public void setRepo(ComponentesRepository repo) {
	this.repo = repo;
    }

    @Override
    public Component findByName(User user, String nombre) {
	log.debug("param nombre " + nombre);
	return repo.findByUserIdAndName(user.getId(), nombre);
    }

    @Override
    public List<Component> findByType(User user, String tipoComponente) {
	log.debug("param tipoComponente " + tipoComponente);
	return repo.findByUserIdAndType(user.getId(), tipoComponente);
    }

    @Override
    public List<Component> find(Component filtro, Sort sort, int page, int numberOfResults) {
	List<Component> list = null;
	if (filtro != null && filtro.getType()!= null && !filtro.getType().isEmpty()){
	    list = findByTipo(filtro, sort, page, numberOfResults);
//	} else if (filtro != null && filtro.getName()!= null && !filtro.getName().isEmpty()){
	    //TODO list = findByName(filtro, sort, page, numberOfResults);
	}
	if(list == null){
	    list = findByUser(filtro.getUser(), sort, page, numberOfResults);
	}
	return list;
    }
    
   private List<Component> findByUser(User user, final Sort sort, final Integer fromPage, final Integer numberOfResults){
	List<Component> list;
	if (fromPage != null){
	    Page<Component> page = repo.findByUserId(user.getId(),new PageRequest(fromPage, numberOfResults, sort));
	    list = page.getContent();
	} else {
	    list = repo.findByUserId(user.getId(),sort);
	}
	return list;
   }
   
   private List<Component> findByTipo(Component component, final Sort sort, final Integer fromPage, final Integer numberOfResults){
       List<Component> list;
       if (component == null){
	   throw new IllegalArgumentException("El componente no puede ser null");
       }
       User user = component.getUser();
       if (fromPage != null){
	   PageRequest pr = new PageRequest(fromPage, numberOfResults, sort);
	   Page<Component> page = repo.findByUserIdAndType(user.getId(), component.getType(),pr);
	   list = page.getContent();
       } else {
	   list = repo.findByUserIdAndType(user.getId(), component.getType(),sort);
       }
       return list;
   }

    @Override
    public long count() {
	return repo.count();
    }

    @Override
    public long count(Component component) {
	long count = 0;
	if (component != null){
	    User user = component.getUser();
	    String type = component.getType();
	    if (user!= null && user.getId()!= null && type!= null){
		count = repo.countByUserIdAndType(user.getId(), type);
	    } else if (user!= null && user.getId()!= null){
		count = repo.countByUserId(user.getId());
	    } else {
		count = repo.count();
	    }
	} else {
	    count = repo.count();
	}
	return count;
    }
    
    public Double getKmComponent(Long idComp) {
	Component pComp = new Component();
	pComp.setId(idComp);
	Component component = findOne(pComp.getId());
	return getKmComponent(component);
    }
    
    public Double getKmComponent(Component component) {
	Double km = 0.0;
	Iterator<ComponentUse> it = component.getComponentUses().iterator();
	while (it.hasNext()) {
	    ComponentUse use = it.next();
	    Date fin = use.getFinish() != null ? use.getFinish() : new Date();
	    List<Activity> list = activityDao.findByBikeAndDates(use.getBike(), use.getInit(), fin);
	    for (Activity act : list) {
		km += act.getKm();
	    }
	}
	return km;
    }
    


    @Override
    public List<Component> getAlerts(User user) {
	List<Component> ret = new ArrayList<Component>();
	//select component from componentuse where sysdate between init and finish (los que estan en uso) 
	List<ComponentUse> uses = componentUsesDAO.findByDate(Calendar.getInstance().getTime());
	Set<Component> compSet= new HashSet<Component>();
	for(ComponentUse cu : uses){
	    compSet.add(cu.getComponent());
	}
	//se recorre el hasset, asi no saseguramos que no duplicamos info (component tiene mismo hascode si id=)
	for(Component component : compSet){
	    Double km = getKmComponent(component.getId());
	    if (km > component.getKmAlert()){
		component.setKmActual(km);
		ret.add(component);
	    }
	}
	return ret;
    }


}
