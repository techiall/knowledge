package top.techial.knowledge.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRepository;
import top.techial.knowledge.dao.NodeRelationRepository;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.NodeRelation;
import top.techial.knowledge.dto.RelationDTO;
import top.techial.knowledge.vo.ParentVO;
import top.techial.knowledge.vo.RelationVO;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<RelationDTO> findByStartNodeName(String name) {
        return nodeRelationRepository.findByStartNodeName(name)
            .stream()
            .map(RelationDTO::new)
            .collect(Collectors.toList());
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
        KnowledgeNode startNode = knowledgeNodeRepository.findFirstByName(relationVO.getStartNode())
            .orElseThrow(NullPointerException::new);
        KnowledgeNode endNode = knowledgeNodeRepository.findFirstByName(relationVO.getEndNode())
            .orElseThrow(NullPointerException::new);
        return nodeRelationRepository.save(new NodeRelation()
            .setStartNode(startNode)
            .setEndNode(endNode)
            .setProperty(relationVO.getProperty()));
    }

    public NodeRelation updateById(Long id, RelationVO relationVO) {
        NodeRelation relation = nodeRelationRepository.findById(id).orElseThrow(NullPointerException::new);
        KnowledgeNode node1 = knowledgeNodeRepository.findFirstByName(relationVO.getStartNode()).orElseThrow(NullPointerException::new);
        KnowledgeNode node2 = knowledgeNodeRepository.findFirstByName(relationVO.getEndNode()).orElseThrow(NullPointerException::new);
        relation.setStartNode(node1).setEndNode(node2).setProperty(relationVO.getProperty());
        return nodeRelationRepository.save(relation);
    }

    public void deleteById(Long id) {
        nodeRelationRepository.deleteById(id);
    }

    public void deleteAll() {
        nodeRelationRepository.deleteAll();
    }

    public KnowledgeNode updateParent(ParentVO parentVO) {
        KnowledgeNode node = knowledgeNodeRepository.findFirstByName(parentVO.getSrcParentName()).orElseThrow(NullPointerException::new);
        if (node.getChildNodes().isEmpty()) {
            throw new IllegalArgumentException();
        }
        KnowledgeNode childNode = knowledgeNodeRepository.findFirstByName(parentVO.getChildName()).orElseThrow(NullPointerException::new);
        node.getChildNodes().remove(childNode);
        knowledgeNodeRepository.save(node);
        KnowledgeNode node1 = knowledgeNodeRepository.findFirstByName(parentVO.getNewParentName()).orElseThrow(NullPointerException::new);
        node1.getChildNodes().add(childNode);
        return knowledgeNodeRepository.save(node1);
    }
}
