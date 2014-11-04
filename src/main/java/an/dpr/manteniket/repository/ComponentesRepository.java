package an.dpr.manteniket.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import an.dpr.manteniket.domain.Component;

/**
 * 
 * @author rsaez
 *
 */
public interface ComponentesRepository extends CrudRepository<Component, Long> {

    Component findByUserIdAndName(Long userId, String name);
    
    List<Component> findAll();
    
    List<Component> findByUserId(Long userId);
    List<Component> findByUserId(Long userId, Sort sort);
    Page<Component> findByUserId(Long userId, Pageable pageable);
    
    List<Component> findByUserIdAndType(Long userId, String type);
    List<Component> findByUserIdAndType(Long userId, String type, Sort sort);
    Page<Component> findByUserIdAndType(Long userId, String type, Pageable pageable);
    
    long count();
    long countByUserId(Long userId);
    long countByUserIdAndType(Long userId, String type);
    
}
