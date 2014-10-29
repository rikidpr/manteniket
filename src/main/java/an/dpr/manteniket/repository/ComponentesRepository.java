package an.dpr.manteniket.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;

/**
 * 
 * @author rsaez
 *
 */
public interface ComponentesRepository extends CrudRepository<Component, Long> {

    Component findByName(String name);

    Component findByType(String type);
    
    List<Component> findAll();
}
