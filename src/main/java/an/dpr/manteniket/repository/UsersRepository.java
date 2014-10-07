package an.dpr.manteniket.repository;

import org.springframework.data.repository.CrudRepository;

import an.dpr.manteniket.domain.User;

public interface UsersRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

}
