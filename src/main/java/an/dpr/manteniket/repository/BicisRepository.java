package an.dpr.manteniket.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.User;

public interface BicisRepository extends CrudRepository<Bici, Long>{

    Bici findByIdBici(Long idBici);
    
    Bici findByCodBici(String codBici);
    
//    List<Bici> findByUserAndTipo(User user, String tipo);
//    List<Bici> findByUserAndTipo(User user, String tipo, Sort sort);
//    Page<Bici> findByUserAndTipo(User user, String tipo, Pageable pageable);

    List<Bici> findByUser(User user);
    List<Bici> findByUser(User user, Sort sort);
    Page<Bici> findByUser(User user, Pageable pageable);

    List<Bici> findAll();
    List<Bici> findAll(Sort sort);
    Page<Bici> findAll(Pageable pageable);
    
    long count();
    long countByUser(User user);
    long countByUserAndTipo(User user, String tipo);
    
}

