package an.dpr.manteniket.dao;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

    public Set<Bici> findByTipoComponente(String tipo) {
	log.debug("param tipo" + tipo);
	return repo.findByTipo(tipo);
    }

    public List<Bici> findAll() {
	return repo.findAll();
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
