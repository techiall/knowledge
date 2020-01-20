package top.techial.knowledge.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.User;

import java.util.Optional;

/**
 * @author techial
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @EntityGraph(attributePaths = "item")
    Optional<User> findFirstByUserName(String name);

    boolean existsByUserName(String name);
}
