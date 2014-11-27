package an.dpr.manteniket.dao;

import java.util.List;

import org.springframework.data.domain.Sort;

import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.domain.User;
import an.dpr.manteniket.exception.ManteniketException;

public interface IComponentsDAO {
    
    Component save(Component comp);
    
    void delete(Long id) throws ManteniketException;

    Component findOne(final Long id) ;

    Component findByName(User user, String nombre);

    List<Component> findByType(User user, String tipoComponente);

    List<Component> findAll();
    List<Component> findAllActives(User user);
    
    List<Component> find(Component filtro, Sort sort, int page, int numberOfResults);

    long count();

    long count(Component component);

    public Double getKmComponent(Long idComp);
    public Double getKmComponent(Component component);
    
    public List<Component> getAlerts(User user);

}
