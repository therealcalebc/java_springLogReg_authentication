/**
 * 
 */
package cd.java.spring.logreg.authentication.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cd.java.spring.logreg.authentication.models.User;

/**
 * @author ccomstock
 *
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByEmail(String email);
	
}
