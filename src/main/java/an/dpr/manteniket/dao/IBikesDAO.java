package an.dpr.manteniket.dao;

import java.util.List;

import org.springframework.data.domain.Sort;

import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.User;
import an.dpr.manteniket.exception.ManteniketException;

public interface IBikesDAO {

    Bici findByIdBici(Long id);
    
    Bici findByCodBici(User user, String codBici);
    
    List<Bici> findAll(User user);
    
    List<Bici> findByTipo(Bici bici, Sort sort, Integer fromPage, Integer numberOfResults);

    long count();

    long count(Bici bici);

    Bici save(Bici bike);

    void delete(Long bikeId) throws ManteniketException;

    List<Bici> find(Bici filtro, Sort sort, int page, int numberOfResults);

}
