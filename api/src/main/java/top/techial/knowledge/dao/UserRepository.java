package top.techial.knowledge.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.User;

import java.util.Optional;

/**
 * @author techial
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findFirstByUserName(String name);

    boolean existsByUserName(String name);
}
