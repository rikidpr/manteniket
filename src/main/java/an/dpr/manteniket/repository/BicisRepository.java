package an.dpr.manteniket.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Bici;

public interface BicisRepository extends CrudRepository<Bici, Long>{

    Bici findByIdBici(Long idBici);
    
    Bici findByCodBici(String codBici);
    
    Set<Bici> findByTipo(String tipo);

    List<Bici> findAll();
    List<Bici> findAll(Sort sort);
    Page<Bici> findAll(Pageable pageable);
}
