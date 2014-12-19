/**
 * 
 */
package an.dpr.manteniket.dao.springdatajpa;

import java.util.List;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import an.dpr.manteniket.dao.IMaintenanceDAO;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.domain.Maintenance;
import an.dpr.manteniket.repository.MaintenancesRepository;

/**
 * @author saez
 *
 */
public class MaintenanceDAOSpringDataJPA extends ManteniketDAO implements IMaintenanceDAO {

    private static final Logger log = LoggerFactory.getLogger(MaintenanceDAOSpringDataJPA.class);
    @Autowired
    private MaintenancesRepository repo;
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IMaintenanceDAO#save(an.dpr.manteniket.domain.Maintenance)
     */
    @Override
    public Maintenance save(Maintenance maintenance) {
	return repo.save(maintenance);
    }

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IMaintenanceDAO#delete(java.lang.Long)
     */
    @Override
    public void delete(Long maintenanceId) {
	repo.delete(maintenanceId);
    }

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IMaintenanceDAO#findById(java.lang.Long)
     */
    @Override
    public Maintenance findById(Long maintenanceId) {
	return repo.findOne(maintenanceId);
    }

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IMaintenanceDAO#count(an.dpr.manteniket.domain.Maintenance)
     */
    @Override
    public long count(Maintenance maintenance) {
	return repo.countByUser(maintenance.getUser());
    }

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IMaintenanceDAO#countByBike(an.dpr.manteniket.domain.Bici)
     */
    @Override
    public long countByBike(Bici bike) {
	return repo.countByBike(bike);
    }

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IMaintenanceDAO#countByComponent(an.dpr.manteniket.domain.Component)
     */
    @Override
    public long countByComponent(Component comp) {
	return repo.countByComponent(comp);
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IMaintenanceDAO#find(an.dpr.manteniket.domain.Maintenance, org.springframework.data.domain.Sort, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<Maintenance> find(final Maintenance maintenance, Sort sort, Integer fromPage, Integer numberOfResults) {
	PageRequest pageRequest = new PageRequest(fromPage, numberOfResults, sort);
	Page<Maintenance> page = repo.findByUser(maintenance.getUser(), pageRequest);
	return loadLazyComponent(page);
    }

    @Override
    public List<Maintenance> findByBike(final Bici bike, Sort sort, Integer fromPage, Integer numberOfResults) {
	final PageRequest pageRequest = new PageRequest(fromPage, numberOfResults, sort);
	Page<Maintenance> page = repo.findByBike(bike, pageRequest);
	return loadLazyComponent(page);
    }

    @Override
    public List<Maintenance> findByComponent(final Component comp, Sort sort, Integer fromPage, Integer numberOfResults) {
	PageRequest pageRequest = new PageRequest(fromPage, numberOfResults, sort);
	Page<Maintenance> page = repo.findByComponent(comp, pageRequest);
	return loadLazyComponent(page);
    }
    
    private List<Maintenance> loadLazyComponent(final Page<Maintenance> page){
	return getTransactionTemplate().execute(new TransactionCallback<List<Maintenance>>(){
	    
	    @Override
	    public List<Maintenance> doInTransaction(TransactionStatus status) {
		List<Maintenance> list = null;
		list = page.getContent();
		for(Maintenance m : list){
		    Hibernate.initialize(m.getComponent());
		}
		return list;
	    }
	    
	});
    }

}
