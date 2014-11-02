package an.dpr.manteniket.dao.springdatajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import an.dpr.manteniket.dao.IUserDAO;
import an.dpr.manteniket.domain.User;
import an.dpr.manteniket.repository.UsersRepository;
import an.dpr.manteniket.security.AppSecurity;

/**
 * Implementacion de IUserDAO con Spring Data JPA
 * @author rsaez
 *
 */
public class UserDAOSpringDataJPA implements IUserDAO {
    
    @Autowired private UsersRepository repo;
    private static final Logger log = LoggerFactory.getLogger(UserDAOSpringDataJPA.class);

    @Override
    public User getUser() {
	String username= AppSecurity.getUserName();
	User user = getUser(username);
	return user;
    }
    
    @Override
    public User getUser(String username) {
	log.debug("inicio");
	return repo.findByUsername(username);
    }

    @Override
    public User getUser(long userId) {
	log.debug("inicio");
	return repo.findById(userId);
    }


}
