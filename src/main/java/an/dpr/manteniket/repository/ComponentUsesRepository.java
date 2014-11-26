/**
 * 
 */
package an.dpr.manteniket.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
 * TODO meter user en todo!
 */
public interface ComponentUsesRepository extends
	CrudRepository<ComponentUse, Long> {

    @Query("FROM ComponentUse cu WHERE cu.bike=:bike")
    List<ComponentUse> findByBike(@Param("bike")Bici bike);
    @Query("FROM ComponentUse cu WHERE cu.bike=:bike")
    List<ComponentUse> findByBike(@Param("bike")Bici bike, Sort sort);
    @Query("FROM ComponentUse cu WHERE cu.bike=:bike")
    Page<ComponentUse> findByBike(@Param("bike")Bici bike, Pageable pageable);

    @Query("FROM ComponentUse cu WHERE cu.component=:component")
    List<ComponentUse> findByComponent(@Param("component") Component component);
    @Query("FROM ComponentUse cu WHERE cu.component=:component")
    List<ComponentUse> findByComponent(@Param("component") Component component, Sort sort);
    @Query("FROM ComponentUse cu WHERE cu.component=:component")
    Page<ComponentUse> findByComponent(@Param("component") Component component, Pageable pageable);
//    
    @Query("FROM ComponentUse cu WHERE cu.init <= :date AND cu.finish >= :date")
    public List<ComponentUse> findByDate(@Param("date")Date date);

//    @Query("select count(cu) FROM ComponentUse cu WHERE cu.bike=:bike")
//    long countByBike(@Param("bike")Bici bike);
    @Query("select count(cu) FROM ComponentUse cu WHERE cu.bike.codBici=:bikeCode")
    long countByBikeCode(@Param("bikeCode")String bikeCode);
//    @Query("select count(cu) FROM ComponentUse cu WHERE cu.component=:component")
//    long countByComponent(Component component);
    @Query("select count(cu) FROM ComponentUse cu WHERE cu.component.name=:componentName")
    long countByComponentName(@Param("componentName") String componentName);
    
    @Query("FROM ComponentUse cu")
    List<ComponentUse> findAll();
    @Query("FROM ComponentUse cu")
    List<ComponentUse> findAll(Sort sort);
    @Query("FROM ComponentUse cu")
    Page<ComponentUse> findAll(Pageable pageable);
    
}
