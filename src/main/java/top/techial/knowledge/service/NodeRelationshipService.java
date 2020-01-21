package top.techial.knowledge.service;

import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.NodeRelationshipRepository;
import top.techial.knowledge.domain.NodeRelationship;

/**
 * @author techial
 */
@Service
public class NodeRelationshipService {
    private final NodeRelationshipRepository nodeRelationshipRepository;

    public NodeRelationshipService(NodeRelationshipRepository nodeRelationshipRepository) {
        this.nodeRelationshipRepository = nodeRelationshipRepository;
    }

    public NodeRelationship save(NodeRelationship nodeRelationship) {
        return nodeRelationshipRepository.save(nodeRelationship);
    }
}
