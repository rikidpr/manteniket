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
    String FINISH_DATE = "finishDate"; 
    String INIT_DATE = "initDate";
    String BIKE= "bike";
    String TYPE="TYPE";
    
    String FIND_BY_USER_ID = "from Activity a where a.disabledDate is null and  a.user.id=:"+USER_ID;
    String COUNT_BY_USER_ID = "SELECT count(a) from Activity a where a.disabledDate is null and  a.user.id=:"+USER_ID;
    String FIND_BETWEEN_DATES = "from Activity a  where a.disabledDate is null and a.date>:"+INIT_DATE
	    +" and a.date<:"+FINISH_DATE;
    String FIND_BIKE_BETWEEN_DATES = "from Activity a  where a.disabledDate is null and a.bike=:"+BIKE
	    +" and a.date>:"+INIT_DATE+" and a.date<:"+FINISH_DATE;
    String FIND_BETWEEN_DATES_AND_TYPE = "from Activity a  where a.disabledDate is null and a.date>:"+INIT_DATE
	    +" and a.date<:"+FINISH_DATE+" and a.type:="+TYPE;

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
    List<Activity> findByDateBetween(@Param(INIT_DATE)Date fehcaIni, @Param(FINISH_DATE) Date fechaFin);
    
    @Query(FIND_BIKE_BETWEEN_DATES)
    List<Activity> findByBikeAndDateBetween(@Param(BIKE)Bici bike, @Param(INIT_DATE)Date fehcaIni, 
	    @Param(FINISH_DATE)Date fechaFin);

    @Query(FIND_BETWEEN_DATES_AND_TYPE)
    List<Activity> findDatesAndType(@Param(INIT_DATE)Date initDate, @Param(FINISH_DATE)Date finisDate, 
	    @Param(TYPE) String type);
    
    @Query(COUNT_BY_USER_ID)
    long countByUserId(@Param(USER_ID) long userId);
    

}
