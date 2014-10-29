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
 * TODO buscar por usuario!!!!!!!!!!!
 */
public class BicisDAO implements IBikesDAO {

    private static final Logger log = LoggerFactory.getLogger(BicisDAO.class);
    @Autowired
    private BicisRepository repo;

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IBikesDAO#findByIdBici(java.lang.Long)
     */
    @Override
    public Bici findByIdBici(Long id) {
	log.debug("params: id " + id);
	return repo.findOne(id);
    }

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IBikesDAO#findByCodBici(java.lang.String)
     */
    @Override
    public Bici findByCodBici(String codBici) {
	log.debug("param codBici " + codBici);
	return repo.findByCodBici(codBici);
    }

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IBikesDAO#findByTipo(java.lang.String)
     */
    @Override
    public List<Bici> findByTipo(String tipo) {
	log.debug("param tipo" + tipo);
	return repo.findByTipo(tipo);
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IBikesDAO#findByTipo(an.dpr.manteniket.domain.Bici, org.springframework.data.domain.Sort, java.lang.Integer, java.lang.Integer)
     */
    @Override
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
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IBikesDAO#findAll()
     */
    @Override
    public List<Bici> findAll(){
	//sort por defecto
	Sort sort = new Sort(Sort.Direction.ASC, "codBici");
	return findAll(sort);
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IBikesDAO#findAll(int, int)
     */
    @Override
    public List<Bici> findAll(int from, int numberOfResults){
	Sort sort = new Sort(Sort.Direction.ASC, "codBici");
	return findAll(sort, from, numberOfResults);
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IBikesDAO#findAll(org.springframework.data.domain.Sort)
     */
    @Override
    public List<Bici> findAll(final Sort sort){
	return findAll(sort, null, null);
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IBikesDAO#findAll(org.springframework.data.domain.Sort, java.lang.Integer, java.lang.Integer)
     */
    @Override
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
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IBikesDAO#count()
     */
    @Override
    public long count(){
	return repo.count();
    }

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IBikesDAO#count(an.dpr.manteniket.domain.Bici)
     */
    @Override
    public long count(Bici bici){
	if (bici == null){
	    return count();
	} else {
	    //de momento el conteo es por tipo porque solo se filtra por tipo, lueog y averemos
	    return repo.countByTipo(bici.getTipo());//TODO ADD USER
	}
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IBikesDAO#save(an.dpr.manteniket.domain.Bici)
     */
    @Override
    public Bici save(Bici bike){
	return repo.save(bike);
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IBikesDAO#delete(java.lang.Long)
     */
    @Override
    public void delete(Long bikeId){
	repo.delete(bikeId);
    }

    public BicisRepository getRepo() {
	return repo;
    }

    public void setRepo(BicisRepository repo) {
	this.repo = repo;
    }

    public List<Bici> find(Bici filtro, Sort sort, int page, int numberOfResults) {
	List<Bici> list;
	if (filtro == null || filtro.getTipo() == null){
	    list = findAll(sort, page, numberOfResults);
	} else {//DE MOMENTO SOLO TIPO
	    list = findByTipo(filtro, sort, page, numberOfResults);
	}
	return list;
    }

}
