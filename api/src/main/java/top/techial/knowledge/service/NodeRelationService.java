package top.techial.knowledge.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRepository;
import top.techial.knowledge.dao.NodeRelationRepository;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.NodeRelation;
import top.techial.knowledge.dto.RelationDTO;
import top.techial.knowledge.mapper.NodeRelationMapper;
import top.techial.knowledge.vo.ParentVO;
import top.techial.knowledge.vo.RelationVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Service
@CacheConfig(cacheNames = "user")
public class NodeRelationService {
    private final NodeRelationRepository nodeRelationRepository;
    private final KnowledgeNodeRepository knowledgeNodeRepository;

    public NodeRelationService(NodeRelationRepository nodeRelationRepository, KnowledgeNodeRepository knowledgeNodeRepository) {
        this.nodeRelationRepository = nodeRelationRepository;
        this.knowledgeNodeRepository = knowledgeNodeRepository;
    }

    @CacheEvict(allEntries = true)
    public Iterable<NodeRelation> saveAll(Iterable<NodeRelation> buildNodeRelation) {
        return nodeRelationRepository.saveAll(buildNodeRelation);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #name", unless = "#result == null")
    public List<RelationDTO> findByStartNodeName(String name) {
        return nodeRelationRepository.findByStartNodeName(name)
            .parallelStream()
            .map(NodeRelationMapper.INSTANCE::toRelationDTO)
            .collect(Collectors.toList());
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #id", unless = "#result == null")
    public List<NodeRelation> findByStartNodeId(Long id) {
        return nodeRelationRepository.findByStartNodeId(id);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #id", unless = "#result == null")
    public NodeRelation findById(Long id) {
        return nodeRelationRepository.findById(id).orElseThrow(NullPointerException::new);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #pageable", unless = "#result == null")
    public Page<NodeRelation> findAll(Pageable pageable) {
        return nodeRelationRepository.findAll(pageable);
    }

    @CacheEvict(allEntries = true)
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

    @CacheEvict(allEntries = true)
    public NodeRelation updateById(Long id, RelationVO relationVO) {
        NodeRelation relation = nodeRelationRepository.findById(id).orElseThrow(NullPointerException::new);
        KnowledgeNode node1 = knowledgeNodeRepository.findFirstByName(relationVO.getStartNode()).orElseThrow(NullPointerException::new);
        KnowledgeNode node2 = knowledgeNodeRepository.findFirstByName(relationVO.getEndNode()).orElseThrow(NullPointerException::new);
        relation.setStartNode(node1).setEndNode(node2).setProperty(relationVO.getProperty());
        return nodeRelationRepository.save(relation);
    }

    @CacheEvict(allEntries = true)
    public void deleteById(Long id) {
        nodeRelationRepository.deleteById(id);
    }

    @CacheEvict(allEntries = true)
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
