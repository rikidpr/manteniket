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

    Bici findByIdBici(Long idBici);
    
    Bici findByCodBici(String codBici);
    

    @Query("FROM Bici b WHERE b.user = :user")
    List<Bici> findByUser(@Param("user") User user);
    @Query("FROM Bici b WHERE b.user = :user")
    List<Bici> findByUser(@Param("user") User user, Sort sort);
    @Query("FROM Bici b WHERE b.user = :user")
    Page<Bici> findByUser(@Param("user") User user, Pageable pageable);

    @Query("FROM Bici b WHERE b.user = :user and b.tipo = :tipo")
    Page<Bici> findByUserAndTipo(@Param("user") User user, @Param("tipo") String tipo, Pageable pageable);

    List<Bici> findAll();
    List<Bici> findAll(Sort sort);
    Page<Bici> findAll(Pageable pageable);
    
    long count();
    @Query("select count(b) FROM Bici b WHERE b.user = :user")
    long countByUser(@Param("user") User user);
    @Query("select count(b) FROM Bici b WHERE b.user = :user and b.tipo=:tipo")
    long countByUserAndTipo(@Param("user") User user, @Param("tipo") String tipo);
    
}

