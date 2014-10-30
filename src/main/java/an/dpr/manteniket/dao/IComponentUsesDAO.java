package an.dpr.manteniket.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;

import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.repository.ComponentUsesRepository;

public interface IComponentUsesDAO {
    
    ComponentUse findOne(Long id);

    ComponentUse save(ComponentUse use);

    void delete(Long id);

    List<ComponentUse> findByBike(Bici bike);

    List<ComponentUse> findByComponent(Component component);

    List<ComponentUse> findByDate(Date date);

    ComponentUsesRepository getRepo();

    void setRepo(ComponentUsesRepository repo);

    long count();

    long count(ComponentUse use);

    List<ComponentUse> find(ComponentUse filtro, Sort sort, int fromPage, int itemsPage);

}