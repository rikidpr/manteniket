package an.dpr.manteniket.repository;

import org.springframework.data.repository.CrudRepository;

import an.dpr.manteniket.domain.User;
import an.dpr.manteniket.domain.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

    User findByUsername(String username);

}
