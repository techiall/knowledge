package top.techial.knowledge.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    @Query(
            nativeQuery = true,
            value = "update user set password = ?2, update_time = now() where id = ?1"
    )
    @Modifying
    void updatePassword(Integer id, String password);

    @Query("select u.password from User u where u.id = ?1")
    String findPasswordById(Integer id);
}
