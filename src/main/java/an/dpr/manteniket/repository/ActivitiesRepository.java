package an.dpr.manteniket.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Bici;

public interface ActivitiesRepository extends CrudRepository<Activity, Long> {
    
    String USER_ID = "USER_ID";
    String FIND_BY_USER_ID = "from Activity a where a.user.id=:"+USER_ID;
    String COUNT_BY_USER_ID = "SELECT count(a) from Activity a where a.user.id=:"+USER_ID;

    @Query("from Activity")
    List<Activity> findAll();
    List<Activity> findAll(Sort sort);
    Page<Activity> findAll(Pageable pageable);

    @Query(FIND_BY_USER_ID)
    List<Activity> findByUserId(@Param(USER_ID) long userId);
    @Query(FIND_BY_USER_ID)
    List<Activity> findByUserId(@Param(USER_ID) long userId, Sort sort);
    @Query(FIND_BY_USER_ID)
    Page<Activity> findByUserId(@Param(USER_ID) long userId, Pageable pageable);
    
    @Query("from Activity a where a.date>:fechaIni and a.date<:fechaFin")//TODO between
    List<Activity> findByDateBetween(@Param("fechaIni")Date fehcaIni, @Param("fechaFin") Date fechaFin);
    
    @Query("from Activity a where a.bike=:bike and  a.date>:fechaIni and a.date<:fechaFin")
    List<Activity> findByBikeAndDateBetween(@Param("bike")Bici bike, @Param("fechaIni")Date fehcaIni, @Param("fechaFin")Date fechaFin);

    @Query(COUNT_BY_USER_ID)
    long countByUserId(@Param(USER_ID) long userId);

}
