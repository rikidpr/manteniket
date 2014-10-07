package an.dpr.manteniket.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Bici;

public interface ActivitiesRepository extends CrudRepository<Activity, Long> {
    
    List<Activity> findAll();
    
    List<Activity> findByDateBetween(Date fehcaIni, Date fechaFin);
    
    List<Activity> findByBikeAndDateBetween(Bici bike, Date fehcaIni, Date fechaFin);

}
