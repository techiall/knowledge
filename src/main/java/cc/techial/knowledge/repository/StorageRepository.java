package cc.techial.knowledge.repository;

import cc.techial.knowledge.domain.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author techial
 */
@Repository
public interface StorageRepository extends JpaRepository<Storage, String>, QuerydslPredicateExecutor<Storage> {
}
