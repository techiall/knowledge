package top.techial.knowledge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.Storage;

/**
 * @author techial
 */
@Repository
public interface StorageRepository extends JpaRepository<Storage, String> {
}
