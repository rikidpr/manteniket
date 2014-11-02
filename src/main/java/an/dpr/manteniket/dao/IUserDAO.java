package an.dpr.manteniket.dao;

import an.dpr.manteniket.domain.User;

/**
 * Interface para la obtencion de informacion sobre eluser de la DB
 * @author rsaez
 *
 */
public interface IUserDAO {

    /**
     * Obtiene el usuario logueado actualmente
     * @return
     */
    public User getUser();
    
    public User getUser(String username);
    
    public User getUser(long userId);
}
