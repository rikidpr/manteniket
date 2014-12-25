package an.dpr.manteniket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.Maintenance;
import an.dpr.manteniket.domain.User;

public interface MaintenancesRepository extends CrudRepository<Maintenance, Long> {

    String COMPONENT = "component";
    String USER = "user";
    String COUNT ="SELECT count(m) ";
    
    String BY_USER = "FROM Maintenance m WHERE m.user=:"+USER;
    String BY_COMPONENT = "FROM Maintenance m WHERE m.component=:"+COMPONENT;
    
    
    @Query(BY_USER)
    Page<Maintenance> findByUser(@Param(USER) User user, Pageable pageable);
    @Query(COUNT+BY_USER)
    long countByUser(@Param(USER) User user);
    
    @Query(BY_COMPONENT)
    Page<Maintenance> findByComponent(@Param(COMPONENT) Component comp, Pageable pageable);
    @Query(COUNT+BY_COMPONENT)
    long countByComponent(@Param(COMPONENT) Component comp);
}
