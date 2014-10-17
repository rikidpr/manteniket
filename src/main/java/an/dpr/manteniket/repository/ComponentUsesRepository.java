/**
 * 
 */
package an.dpr.manteniket.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;

/**
 * Repository of ComponentUse data
 * 
 * @author rsaez
 * 
 */
public interface ComponentUsesRepository extends
	CrudRepository<ComponentUse, Long> {

    @Query("FROM ComponentUse cu WHERE cu.bike=:bike")
    public List<ComponentUse> findByBike(@Param("bike")Bici bike);

    @Query("FROM ComponentUse cu WHERE cu.component=:component")
    public List<ComponentUse> findByComponent(@Param("component") Component component);
    
    @Query("FROM ComponentUse cu WHERE cu.init <= :date AND cu.finish >= :date")
    public List<ComponentUse> findByDate(@Param("date")Date date);

}
