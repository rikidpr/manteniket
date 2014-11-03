package an.dpr.manteniket.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.User;
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

    @Override
    public Bici findByIdBici(Long id) {
	log.debug("params: id " + id);
	return repo.findOne(id);
    }

    @Override
    public Bici findByCodBici(User user, String codBici) {
	log.debug("param codBici " + codBici);
	return repo.findByCodBici(user, codBici);
    }

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IBikesDAO#findByTipo(an.dpr.manteniket.domain.Bici, org.springframework.data.domain.Sort, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<Bici> findByTipo(Bici bici, final Sort sort, final Integer fromPage, final Integer numberOfResults){
   	List<Bici> list;
   	if (fromPage != null){
   	    Page<Bici> page = repo.findByUserAndTipo(bici.getUser(),bici.getTipo(), new PageRequest(fromPage, numberOfResults, sort));
   	    list = page.getContent();
   	} else {
   	    list = repo.findByUserAndTipo(bici.getUser(),bici.getTipo(),sort);
   	}
   	return list;
       }
    
    public List<Bici> findAll(User user){
	//sort por defecto
	Sort sort = new Sort(Sort.Direction.ASC, "codBici");
	Bici bici = new Bici();
	bici.setUser(user);
	Integer count = (int) count(bici);
	return findAll(user, sort, 0, count );
    }
    
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IBikesDAO#findAll(org.springframework.data.domain.Sort, java.lang.Integer, java.lang.Integer)
     */
    public List<Bici> findAll(User user, final Sort sort, final Integer fromPage, final Integer numberOfResults){
	List<Bici> list;
	if (fromPage != null){
	    Page<Bici> page = repo.findByUser(user,new PageRequest(fromPage, numberOfResults, sort));
	    list = page.getContent();
	} else {
	    list = repo.findByUser(user,sort);
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
	    return repo.countByUserAndTipo(bici.getUser(),bici.getTipo());
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
	//TODO manejar excepciones. Peude suceder que no permitar borrarlo por dependencias de FK.
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
	if (filtro == null){
	    list = null;
	} else if (filtro.getTipo() == null){
	    list = findAll(filtro.getUser(), sort, page, numberOfResults);
	} else {//DE MOMENTO SOLO TIPO
	    list = findByTipo(filtro, sort, page, numberOfResults);
	}
	return list;
    }
    
}
