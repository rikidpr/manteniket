package an.dpr.manteniket.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.domain.User;
import an.dpr.manteniket.repository.ComponentesRepository;

/**
 * Dao del entity componentes
 * 
 * @author rsaez
 * TODO BUSCAR POR USUARIOOOOOO
 */
public class ComponentesDAO extends ManteniketDAO implements IComponentsDAO{

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


}
