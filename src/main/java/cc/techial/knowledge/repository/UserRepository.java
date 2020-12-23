package cc.techial.knowledge.repository;

import cc.techial.knowledge.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author techial
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, QuerydslPredicateExecutor<User> {

    @EntityGraph(attributePaths = "item")
    Optional<User> findFirstByUserName(String name);

    boolean existsByUserName(String name);
}
