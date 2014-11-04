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

    String USER = "user";
    String COD_BICI="codBici";
    String TIPO = "tipo";
    String QUERY_USER= "select b from Bici b where b.user =:"+USER; 
    String QUERY_USER_TIPO = "select b from Bici b where b.user =:"+USER+" and b.tipo =:"+TIPO; 
    String QUERY_USER_COD_BICI = "select b from Bici b where b.user =:"+USER+" and b.codBici =:"+COD_BICI; 
    String QUERY_COUNT_USER= "select count(b) from Bici b  where b.user =:"+USER; 
    String QUERY_COUNT_USER_TIPO = "select count(b) from Bici b where b.user =:"+USER+" and b.tipo =:"+TIPO; 
    
    Bici findByIdBici(Long idBici);
    
    @Query(QUERY_USER_COD_BICI)
    Bici findByCodBici(@Param(USER) User user, @Param(COD_BICI) String codBici);
    
    
    @Query(QUERY_USER_TIPO)
    List<Bici> findByUserAndTipo(@Param(USER) User user, @Param(TIPO) String tipo);
    @Query(QUERY_USER_TIPO)
    List<Bici> findByUserAndTipo(@Param(USER) User user, @Param(TIPO) String tipo, Sort sort);
    @Query(QUERY_USER_TIPO)
    Page<Bici> findByUserAndTipo(@Param(USER) User user, @Param(TIPO) String tipo, Pageable pageable);

    @Query(QUERY_USER)
    List<Bici> findByUser(@Param(USER) User user);
    @Query(QUERY_USER)
    List<Bici> findByUser(@Param(USER) User user, Sort sort);
    @Query(QUERY_USER)
    Page<Bici> findByUser(@Param(USER) User user, Pageable pageable);

    @Deprecated
    List<Bici> findAll();
    @Deprecated
    List<Bici> findAll(Sort sort);
    @Deprecated
    Page<Bici> findAll(Pageable pageable);
    
    long count();
    @Query(QUERY_COUNT_USER)
    long countByUser(@Param(USER) User user);
    @Query(QUERY_COUNT_USER_TIPO)
    long countByUserAndTipo(@Param(USER) User user, @Param(TIPO) String tipo);
    
}

