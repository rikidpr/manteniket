package an.dpr.manteniket.dao;

import java.util.List;

import org.springframework.data.domain.Sort;

import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.User;

public interface IComponentsDAO {
    
    Component save(Component comp);
    
    void delete(Long id);

    Component findOne(final Long id) ;

    Component findByName(User user, String nombre);

    List<Component> findByType(User user, String tipoComponente);

    List<Component> findAll();
    
    List<Component> find(Component filtro, Sort sort, int page, int numberOfResults);

    long count();

    long count(Component component);

}
