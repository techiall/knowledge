package top.techial.knowledge.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.NodeText;

@Repository
public interface NodeTextRepository extends JpaRepository<NodeText, Long> {
}
