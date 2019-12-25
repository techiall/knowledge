package top.techial.knowledge.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.Storage;

import java.util.Optional;

/**
 * @author techial
 */
@Repository
public interface StorageRepository extends JpaRepository<Storage, Integer> {
    Optional<Storage> findFirstBySha1(String sha1);
}
