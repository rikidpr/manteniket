package an.dpr.manteniket.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.domain.User;
import an.dpr.manteniket.repository.BicisRepository;
import an.dpr.manteniket.repository.ComponentUsesRepository;
import an.dpr.manteniket.repository.ComponentesRepository;
import an.dpr.manteniket.util.DateUtil;

public class ComponentUsesDAO extends ManteniketDAO implements IComponentUsesDAO {

    private static final Logger log = LoggerFactory.getLogger(ComponentUsesDAO.class);
    @Autowired
    private ComponentUsesRepository repo;
    @Autowired
    private BicisRepository bikeRepo;
    @Autowired
    private ComponentesRepository compRepo;
    @Autowired IBikesDAO bikesDao;
    @Autowired IComponentsDAO componentsDao;
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#findOne(java.lang.Long)
     */
    @Override
    public ComponentUse findOne(final Long id){
	return getTransactionTemplate().execute(new TransactionCallback<ComponentUse>(){

	    @Override
	    public ComponentUse doInTransaction(TransactionStatus status) {
		ComponentUse cu = repo.findOne(id);
		Hibernate.initialize(cu.getBike());
		Hibernate.initialize(cu.getComponent());
		return cu;
	    }
	
	});
	//TODO excepciones sql
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#save(an.dpr.manteniket.domain.ComponentUse)
     */
    @Override
    public ComponentUse save(ComponentUse use){
	//se guardan todas las fechas a hora 00:00
	try {
	    use.setInit(DateUtil.dateWithoutHour(use.getInit()));
	    use.setFinish(DateUtil.dateWithoutHour(use.getFinish()));
	} catch (ParseException e) {
	    log.error("Error parseando fechas de "+use);
	}
	
	return repo.save(use);
	//TODO excepciones sql
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#delete(java.lang.Long)
     */
    @Override
    public void delete(Long id){
	repo.delete(id);
	//TODO excepciones sql
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#findByBike(an.dpr.manteniket.domain.Bici)
     */
    @Override
    public List<ComponentUse> findByBike(final Bici bike){
	return getTransactionTemplate().execute(
		new TransactionCallback<List<ComponentUse>>(){
	    
	    @Override
	    public List<ComponentUse> doInTransaction(TransactionStatus status) {
		List<ComponentUse> list = repo.findByBike(bike);
		for (ComponentUse cu : list){
		    Hibernate.initialize(cu.getBike());
		    Hibernate.initialize(cu.getComponent());
		}
		return list;
	    }
	    
	});
	//TODO excepciones sql
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#findByComponent(an.dpr.manteniket.domain.Component)
     */
    @Override
    public List<ComponentUse> findByComponent(final Component component){
	return getTransactionTemplate().execute(
		new TransactionCallback<List<ComponentUse>>(){
		    
		    @Override
		    public List<ComponentUse> doInTransaction(TransactionStatus status) {
			List<ComponentUse> list = repo.findByComponent(component);
			for (ComponentUse cu : list){
			    Hibernate.initialize(cu.getBike());
			    Hibernate.initialize(cu.getComponent());
			}
			return list;
		    }
		    
		});
	//TODO excepciones sql
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#findByDate(java.util.Date)
     */
    @Override
    public List<ComponentUse> findByDate(final Date date){
	return getTransactionTemplate().execute(
		new TransactionCallback<List<ComponentUse>>(){
		    
		    @Override
		    public List<ComponentUse> doInTransaction(TransactionStatus status) {
			List<ComponentUse> list = repo.findByDate(date);
			for (ComponentUse cu : list){
			    Hibernate.initialize(cu.getBike());
			    Hibernate.initialize(cu.getComponent());
			}
			return list;
		    }
		    
		});
	//TODO excepciones sql
    }

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#getRepo()
     */
    @Override
    public ComponentUsesRepository getRepo() {
        return repo;
    }

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#setRepo(an.dpr.manteniket.repository.ComponentUsesRepository)
     */
    @Override
    public void setRepo(ComponentUsesRepository repo) {
        this.repo = repo;
    }

    @Override
    public long count() {
	return repo.count();
    }

    @Override
    public long count(ComponentUse use) {
	long count = 0;
	if (use != null &&use.getBike()!=null){
	    count = repo.countByBikeCode(use.getBike().getCodBici());
	    
	} else if (use != null && use.getComponent()!=null){
	    count = repo.countByComponentName(use.getComponent().getName());
	    
	} else {
	    count = count();
	}
	return count;
    }

    @Override
    public List<ComponentUse> find(final ComponentUse filtro, final Sort sort, final int fromPage, final int itemsPage) {
	return getTransactionTemplate().execute(
		new TransactionCallback<List<ComponentUse>>(){
		    
		    @Override
		    public List<ComponentUse> doInTransaction(TransactionStatus status) {
			List<ComponentUse> list = findLazy(filtro, sort, fromPage, itemsPage);
			for (ComponentUse cu : list){
			    Hibernate.initialize(cu.getBike());
			    Hibernate.initialize(cu.getComponent());
			}
			return list;
		    }
		    
		});
    }
    
    public List<ComponentUse> findLazy(ComponentUse filtro, Sort sort, int fromPage, int itemsPage) {
	List<ComponentUse> list;
	Page<ComponentUse> page;
	PageRequest pageRequest = new PageRequest(fromPage, itemsPage, sort);
	
	if (filtro != null && filtro.getBike() != null){
	    Bici bike = null;
	    if (filtro.getBike().getId() != null)
		bike = filtro.getBike();
	    else if (filtro.getBike().getCodBici() != null){
		bike = bikeRepo.findByCodBici(filtro.getUser(), filtro.getBike().getCodBici());
	    }
	    page = repo.findByBike(bike, pageRequest);
	    
	} else if (filtro != null && filtro.getComponent() != null){
	    Component fComp = filtro.getComponent();
	    Component comp = null;
	    if (fComp.getId()!=null){
		comp = fComp;
	    } else if (fComp.getName()!=null && !fComp.getName().isEmpty()){
		comp = compRepo.findByUserIdAndName(filtro.getUser().getId(), fComp.getName());
	    }
	    page = repo.findByComponent(comp, pageRequest);
	    
	}  else {
	    page = repo.find(filtro.getUser().getId(), pageRequest);
	    list = page.getContent();
	}
	list = page.getContent();
	return list;
    }

}
