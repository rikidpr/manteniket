package an.dpr.manteniket.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.repository.ComponentesRepository;

/**
 * Dao del entity componentes
 * 
 * @author rsaez
 * TODO BUSCAR POR USUARIOOOOOO
 */
public class ComponentesDAO extends ManteniketDAO{

    private static final Logger log = LoggerFactory
	    .getLogger(ComponentesDAO.class);
    @Autowired
    private ComponentesRepository repo;
    
    public Component save(Component comp){
	return repo.save(comp);
	// TODO manejo excepciones sql
    }
    
    public void delete(Long id){
	repo.delete(id);
	// TODO manejo excepciones sql
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

    public Component findByName(String nombre) {
	log.debug("param nombre " + nombre);
	return repo.findByName(nombre);
	// TODO manejo excepciones sql
    }

    public Component findByType(String tipoComponente) {
	log.debug("param tipoComponente " + tipoComponente);
	return repo.findByType(tipoComponente);
	// TODO manejo excepciones sql
    }

    public List<Component> findAll() {
	return repo.findAll();
	// TODO manejo excepciones sql
    }
    public ComponentesRepository getRepo() {
	return repo;
    }

    public void setRepo(ComponentesRepository repo) {
	this.repo = repo;
    }


}
