package an.dpr.manteniket.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.User;

public interface BicisRepository extends CrudRepository<Bici, Long>{
    
    String QUERY_USER= "select b from Bici b where b.user =:user"; 
    String QUERY_USER_TIPO = "select b from Bici b where b.user =:user and b.tipo =:tipo"; 
    String QUERY_COUNT_USER= "select count(b) from Bici b  where b.user =:user"; 
    String QUERY_COUNT_USER_TIPO = "select count(b) from Bici b where b.user =:user and b.tipo =:tipo";
    
    Bici findByIdBici(Long idBici);
    
    Bici findByCodBici(User user, String codBici);
    
    
    @Query(QUERY_USER_TIPO)
    List<Bici> findByUserAndTipo(@Param("user") User user, @Param("tipo") String tipo);
    @Query(QUERY_USER_TIPO)
    List<Bici> findByUserAndTipo(@Param("user") User user, @Param("tipo") String tipo, Sort sort);
    @Query(QUERY_USER_TIPO)
    Page<Bici> findByUserAndTipo(@Param("user") User user, @Param("tipo") String tipo, Pageable pageable);

    @Query(QUERY_USER)
    List<Bici> findByUser(@Param("user") User user);
    @Query(QUERY_USER)
    List<Bici> findByUser(@Param("user") User user, Sort sort);
    @Query(QUERY_USER)
    Page<Bici> findByUser(@Param("user") User user, Pageable pageable);

    @Deprecated
    List<Bici> findAll();
    @Deprecated
    List<Bici> findAll(Sort sort);
    @Deprecated
    Page<Bici> findAll(Pageable pageable);
    
    long count();
    @Query(QUERY_COUNT_USER)
    long countByUser(@Param("user") User user);
    @Query(QUERY_COUNT_USER_TIPO)
    long countByUserAndTipo(@Param("user") User user, @Param("tipo") String tipo);
    
}

