/**
 * 
 */
package an.dpr.manteniket.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.domain.Component;

/**
 * Repository of ComponentUse data
 * 
 * @author rsaez
 * 
 */
public interface ComponentUsesRepository extends
	CrudRepository<ComponentUse, Long> {

    public List<ComponentUse> findByBike(Bici bike);

    public List<ComponentUse> findByComponent(Component component);
    
    @Query("FROM ComponentUse cu WHERE cu.init <= date AND cu.finish >= date")
    public List<ComponentUse> findByDate(Date date);

}
