package an.dpr.manteniket.dao;

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
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.repository.BicisRepository;

/**
 * Dao del entity componentes
 * 
 * @author rsaez
 * 
 */
public class BicisDAO {

    private static final Logger log = LoggerFactory.getLogger(BicisDAO.class);
    @Autowired
    private BicisRepository repo;

    public Bici findByIdBici(Long id) {
	log.debug("params: id " + id);
	return repo.findOne(id);
    }

    public Bici findByCodBici(String codBici) {
	log.debug("param codBici " + codBici);
	return repo.findByCodBici(codBici);
    }

    public List<Bici> findByTipo(String tipo) {
	log.debug("param tipo" + tipo);
	return repo.findByTipo(tipo);
    }
    
    public List<Bici> findByTipo(Bici bici, final Sort sort, final Integer fromPage, final Integer numberOfResults){
   	List<Bici> list;
   	if (fromPage != null){
   	    Page<Bici> page = repo.findByTipo(bici.getTipo(), new PageRequest(fromPage, numberOfResults, sort));
   	    list = page.getContent();
   	} else {
   	    list = repo.findByTipo(bici.getTipo(),sort);
   	}
   	return list;
       }
    
    public List<Bici> findAll(){
	//sort por defecto
	Sort sort = new Sort(Sort.Direction.ASC, "codBici");
	return findAll(sort);
    }
    
    public List<Bici> findAll(int from, int numberOfResults){
	Sort sort = new Sort(Sort.Direction.ASC, "codBici");
	return findAll(sort, from, numberOfResults);
    }
    
    public List<Bici> findAll(final Sort sort){
	return findAll(sort, null, null);
    }
    
    public List<Bici> findAll(final Sort sort, final Integer fromPage, final Integer numberOfResults){
	List<Bici> list;
	if (fromPage != null){
	    Page<Bici> page = repo.findAll(new PageRequest(fromPage, numberOfResults, sort));
	    list = page.getContent();
	} else {
	    list = repo.findAll(sort);
	}
	return list;
    }
    
    public Bici save(Bici bike){
	return repo.save(bike);
    }
    
    public void delete(Long bikeId){
	repo.delete(bikeId);
    }

    public BicisRepository getRepo() {
	return repo;
    }

    public void setRepo(BicisRepository repo) {
	this.repo = repo;
    }

}
