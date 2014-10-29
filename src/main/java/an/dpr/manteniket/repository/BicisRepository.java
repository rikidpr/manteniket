package an.dpr.manteniket.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import an.dpr.manteniket.domain.Bici;

public interface BicisRepository extends CrudRepository<Bici, Long>{

    Bici findByIdBici(Long idBici);
    
    Bici findByCodBici(String codBici);
    
    List<Bici> findByTipo(String tipo);
    List<Bici> findByTipo(String tipo, Sort sort);
    Page<Bici> findByTipo(String tipo, Pageable pageable);

    List<Bici> findAll();
    List<Bici> findAll(Sort sort);
    Page<Bici> findAll(Pageable pageable);
    
    long count();
    long countByTipo(String tipo);
    
}

