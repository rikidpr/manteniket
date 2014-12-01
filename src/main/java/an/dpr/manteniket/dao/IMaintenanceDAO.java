package an.dpr.manteniket.dao;

import java.util.List;

import org.springframework.data.domain.Sort;

import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.Maintenance;

public interface IMaintenanceDAO {
    
    Maintenance save(Maintenance maintenance);
    void delete(Long maintenanceId);

    Maintenance findById(Long maintenanceId);
    List<Maintenance> find(Maintenance maintenance, Sort sort, Integer fromPage, Integer numberOfResults);
    List<Maintenance> findByBike(Bici bike, Sort sort, Integer fromPage, Integer numberOfResults);
    List<Maintenance> findByComponent(Component comp, Sort sort, Integer fromPage, Integer numberOfResults);

    long count(Maintenance maintenance);
    long countByBike(Bici bike);
    long countByComponent(Component comp);

}
