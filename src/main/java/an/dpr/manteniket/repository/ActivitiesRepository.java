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
    String FECHA_FIN = "fechaFin"; 
    String FECHA_INI = "fechaIni";
    String BIKE= "bike";
    
    String FIND_BY_USER_ID = "from Activity a where a.disabledDate is null and  a.user.id=:"+USER_ID;
    String COUNT_BY_USER_ID = "SELECT count(a) from Activity a where a.disabledDate is null and  a.user.id=:"+USER_ID;
    String FIND_BETWEEN_DATES = "from Activity a  where a.disabledDate is null and a.date>:"+FECHA_INI+" and a.date<:"+FECHA_FIN;
    String FIND_BIKE_BETWEEN_DATES = "from Activity a  where a.disabledDate is null and a.bike=:"+BIKE+" and a.date>:"+FECHA_INI+" and a.date<:"+FECHA_FIN;

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
    
    @Query(FIND_BETWEEN_DATES)
    List<Activity> findByDateBetween(@Param(FECHA_INI)Date fehcaIni, @Param(FECHA_FIN) Date fechaFin);
    
    @Query(FIND_BIKE_BETWEEN_DATES)
    List<Activity> findByBikeAndDateBetween(@Param(BIKE)Bici bike, @Param(FECHA_INI)Date fehcaIni, @Param(FECHA_FIN)Date fechaFin);

    @Query(COUNT_BY_USER_ID)
    long countByUserId(@Param(USER_ID) long userId);

}
