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

import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.repository.ComponentUsesRepository;

public class ComponentUsesDAO {

    private static final Logger log = LoggerFactory.getLogger(ComponentUsesDAO.class);
    @Autowired
    private ComponentUsesRepository repo;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;
    
    private TransactionTemplate getTransactionTemplate(){
	if (transactionTemplate == null){
	    transactionTemplate = new TransactionTemplate(transactionManager);
	}
	return transactionTemplate;
    }
    
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
    
    public ComponentUse save(ComponentUse use){
	return repo.save(use);
	//TODO excepciones sql
    }
    
    public void delete(Long id){
	repo.delete(id);
	//TODO excepciones sql
    }
    
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

    public ComponentUsesRepository getRepo() {
        return repo;
    }

    public void setRepo(ComponentUsesRepository repo) {
        this.repo = repo;
    }
}
