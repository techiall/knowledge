package top.techial.knowledge.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRepository;
import top.techial.knowledge.dao.NodeRelationRepository;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.NodeRelation;
import top.techial.knowledge.vo.RelationVO;

import java.util.List;

/**
 * @author techial
 */
@Service
public class NodeRelationService {
    private final NodeRelationRepository nodeRelationRepository;
    private final KnowledgeNodeRepository knowledgeNodeRepository;


    public NodeRelationService(NodeRelationRepository nodeRelationRepository, KnowledgeNodeRepository knowledgeNodeRepository) {
        this.nodeRelationRepository = nodeRelationRepository;
        this.knowledgeNodeRepository = knowledgeNodeRepository;
    }

    public Iterable<NodeRelation> saveAll(Iterable<NodeRelation> buildNodeRelation) {
        return nodeRelationRepository.saveAll(buildNodeRelation);
    }

    public List<NodeRelation> findByStartNodeName(String name) {
        return nodeRelationRepository.findByStartNodeName(name);
    }

    public List<NodeRelation> findByStartNodeId(Long id) {
        return nodeRelationRepository.findByStartNodeId(id);
    }

    public NodeRelation findById(Long id) {
        return nodeRelationRepository.findById(id).orElseThrow(NullPointerException::new);
    }

    public Page<NodeRelation> findAll(Pageable pageable) {
        return nodeRelationRepository.findAll(pageable);
    }

    public long count() {
        return nodeRelationRepository.count();
    }

    public NodeRelation save(RelationVO relationVO) {
        KnowledgeNode startNode = knowledgeNodeRepository.findFirstByName(relationVO.getStartNode()).orElseThrow(NullPointerException::new);
        KnowledgeNode endNode = knowledgeNodeRepository.findFirstByName(relationVO.getEndNodeName()).orElseThrow(NullPointerException::new);
        return nodeRelationRepository.save(new NodeRelation()
            .setStartNode(startNode)
            .setEndNode(endNode)
            .setProperty(relationVO.getProperty()));
    }

    public NodeRelation updateById(Long id, RelationVO relationVO) {
        return nodeRelationRepository.findById(id).orElseThrow(NullPointerException::new)
            .setProperty(relationVO.getProperty());
    }

    public void deleteById(Long id) {
        nodeRelationRepository.deleteById(id);
    }

    public void deleteAll() {
        nodeRelationRepository.deleteAll();
    }
}
